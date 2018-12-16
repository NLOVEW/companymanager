package com.linghong.companymanager.service;

import com.linghong.companymanager.constant.UrlConstant;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.CompanyTutor;
import com.linghong.companymanager.pojo.DiscussMessage;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.repository.CompanyRepository;
import com.linghong.companymanager.repository.CompanyTutorRepository;
import com.linghong.companymanager.repository.DiscussMessageRepository;
import com.linghong.companymanager.repository.UserRepository;
import com.linghong.companymanager.utils.FastDfsUtil;
import com.linghong.companymanager.utils.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/12 13:31
 * @Version 1.0
 * @Description:
 */
@Service
public class CompanyTutorService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private CompanyTutorRepository companyTutorRepository;
    @Resource
    private CompanyRepository companyRepository;
    @Resource
    private DiscussMessageRepository discussMessageRepository;
    @Resource
    private UserRepository userRepository;

    @RabbitListener(queues = "companyTutor")
    private void handlerCompanyTutorByRabbitMq(Long companyId) {
        logger.info("接收到rabbitMq发布的CompanyId:" + companyId);
        Company company = companyRepository.findById(companyId).get();
        CompanyTutor companyTutor = new CompanyTutor();
        companyTutor.setCompanyTutorId(IDUtil.getId());
        companyTutor.setMessage(company.getBusinessScope() + "/" + company.getCompanyScope());
        companyTutor.setType(company.getCompanyType());
        companyTutor.setTitle(company.getCompanyName());
        companyTutor.setPushTime(new Date());
        companyTutor.setTutorCompany(company);
        companyTutor = companyTutorRepository.save(companyTutor);
        logger.info("发布公司帮手成功" + companyTutor.toString());
    }

    public boolean pushCompanyTutor(CompanyTutor companyTutor, Company company) {
        companyTutor.setTutorCompany(company);
        companyTutor.setCompanyTutorId(IDUtil.getId());
        companyTutorRepository.save(companyTutor);
        return true;
    }

    public List<CompanyTutor> getCompanyTutorByCompanyType(String city, String type) {
        try {
            Specification<CompanyTutor> specification = (root, query, builder) -> {
                Predicate title = builder.like(root.get("title").as(String.class), "%" + type + "%");
                Predicate type1 = builder.like(root.get("type").as(String.class), "%" + type + "%");
                Predicate message = builder.like(root.get("message").as(String.class), "%" + type + "%");
                Predicate predicate = builder.or(title, type1, message);
                return predicate;
            };
            List<CompanyTutor> tutors = companyTutorRepository.findAll(specification);
            if (city != null) {
                tutors = tutors.stream().filter(companyTutor -> {
                    if (companyTutor.getTutorCompany().getAddress().contains(city)) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
            }
            return tutors;
        } catch (Exception e) {
            logger.error("------解码出错------");
        }
        return null;
    }


    public Map<String,List<User>> getLaw(String city) {
        try {
            Specification<CompanyTutor> specification = (root, query, builder) -> {
                Predicate title = builder.like(root.get("title").as(String.class), "%法律%");
                Predicate type1 = builder.like(root.get("type").as(String.class), "%法律%");
                Predicate message = builder.like(root.get("message").as(String.class), "%法律%");
                Predicate predicate = builder.or(title, type1, message);
                return predicate;
            };
            List<CompanyTutor> tutors = companyTutorRepository.findAll(specification);
            if (city != null) {
                tutors = tutors.stream().filter(companyTutor -> {
                    if (companyTutor.getTutorCompany().getAddress().contains(city)) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
            }
            Map<String,List<User>> result = new HashMap<>();
            tutors.stream().forEach(tu->{
                List<User> all = userRepository.findAllByFromCompany_CompanyId(tu.getTutorCompany().getCompanyId());
                result.put(tu.getTutorCompany().getCompanyName(), all);
            });
            return result;
        } catch (Exception e) {
            logger.error("------解码出错------");
        }
        return null;
    }

    public boolean pushDiscussMessage(DiscussMessage discussMessage,
                                      String companyTutorId,
                                      String baseImage,
                                      User user,
                                      Company company) {
        if (user != null) {
            discussMessage.setFromUser(user);
        } else {
            discussMessage.setFromCompany(company);
        }
        if (baseImage != null) {
            discussMessage.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(baseImage));
        }
        if (discussMessage.getDiscussMessageId() != null) {
            DiscussMessage target = discussMessageRepository.findById(discussMessage.getDiscussMessageId()).get();
            if (target.getFromCompany() != null) {
                discussMessage.setToCompany(target.getFromCompany());
            } else {
                discussMessage.setToUser(target.getFromUser());
            }
        }
        discussMessage.setPushTime(new Date());
        discussMessage.setDiscussMessageId(IDUtil.getId());
        CompanyTutor companyTutor = companyTutorRepository.findById(companyTutorId).get();
        Set<DiscussMessage> discussMessages = companyTutor.getDiscussMessages();
        discussMessages.add(discussMessage);
        companyTutor.setDiscussMessages(discussMessages);
        return true;
    }
}
