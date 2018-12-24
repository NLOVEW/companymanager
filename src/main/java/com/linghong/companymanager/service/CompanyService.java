package com.linghong.companymanager.service;

import com.linghong.companymanager.constant.UrlConstant;
import com.linghong.companymanager.pojo.*;
import com.linghong.companymanager.repository.CompanyRepository;
import com.linghong.companymanager.repository.UserRepository;
import com.linghong.companymanager.utils.BeanUtil;
import com.linghong.companymanager.utils.FastDfsUtil;
import com.linghong.companymanager.utils.IDUtil;
import com.linghong.companymanager.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/8 12:15
 * @Version 1.0
 * @Description:
 */
@Service
public class CompanyService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private CompanyRepository companyRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private AmqpTemplate amqpTemplate;
    @Resource
    private IMServiceImpl imServiceImpl;

    public Company register(Company company) {
        Company target = companyRepository.findByMobilePhone(company.getMobilePhone());
        if (target != null){
            return null;
        }
        company.setCreateTime(new Date());
        company.setPassword(MD5Util.md5(company.getPassword()));
        company.setAuth(false);
        company = companyRepository.save(company);
        String mobilePhone = company.getMobilePhone();
        new Thread(
                ()->{
                    try {
                        //注册即时通信
                        CreateUser createUser = new CreateUser();
                        //使用手机号当唯一值
                        createUser.setAccid(mobilePhone);
                        imServiceImpl.createUser(createUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();
        if (company.getCompanyId() != null) {
            Wallet wallet = new Wallet();
            wallet.setCompany(company);
            wallet.setCreateTime(new Date());
            wallet.setWalletId(IDUtil.getId());
            wallet.setBalance(new BigDecimal(0));
            return company;
        }
        return null;
    }

    public Company perfectUserMessage(Company company,
                                      Company sessionCompany,
                                      String base64Avatar,
                                      String base64BusinessLicense,
                                      String base64Images) {
        logger.info("上传："+company.toString());
        sessionCompany = companyRepository.findById(sessionCompany.getCompanyId()).get();
        logger.info("数据库："+sessionCompany.toString());
        if (base64Avatar != null) {
            sessionCompany.setAvatar(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadImage(base64Avatar));
        }
        if (base64BusinessLicense != null) {
            sessionCompany.setBusinessLicense(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(base64BusinessLicense));
        }
        if (base64Images != null) {
            String[] split = base64Images.split("。");
            Set<Image> images = new HashSet<>();
            for (String imagePath : split) {
                Image image = new Image();
                image.setCreateTime(new Date());
                image.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(imagePath));
                images.add(image);
            }
            sessionCompany.setImages(images);
        }
        BeanUtil.copyPropertiesIgnoreNull(company, sessionCompany);
        companyRepository.save(sessionCompany);
        //信息完善后自动发布公司的业务到市场
        amqpTemplate.convertSendAndReceive("companyTutor", sessionCompany.getCompanyId());
        return sessionCompany;
    }

    public Company getCompanyByCompanyId(Long companyId) {
        return companyRepository.findById(companyId).get();
    }

    public List<Company> getCompanyByKey(String city, String key) {
        try {
            Specification<Company> specification = (root, query, builder) -> {
                Predicate companyName = builder.like(root.get("companyName").as(String.class
                ), "%" + key + "%");
                Predicate userName = builder.like(root.get("userName").as(String.class
                ), "%" + key + "%");
                Predicate businessScope = builder.like(root.get("businessScope").as(String.class
                ), "%" + key + "%");
                Predicate companyScope = builder.like(root.get("companyScope").as(String.class
                ), "%" + key + "%");
                Predicate companyType = builder.like(root.get("companyType").as(String.class
                ), "%" + key + "%");
                Predicate predicate = builder.or(companyName, userName, businessScope, companyScope, companyType);
                return predicate;
            };
            List<Company> companies = companyRepository.findAll(specification);
            if (city != null) {
                companies = companies.stream().filter(company -> {
                    if (company.getAuth() && company.getAddress().contains(city)) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
            }
            return companies;
        } catch (Exception e) {
            logger.error("解码失败");
        }
        return null;
    }

    public boolean pushDepartments(String department, Company company) {
        company = companyRepository.findById(company.getCompanyId()).get();
        String[] departments = company.getDepartments();
        String[] target = null;
        if (departments != null && departments.length > 0){
            target = new String[departments.length + 1];
            for (int i = 0; i < departments.length; i++) {
                target[i] = departments[i];
            }
            target[departments.length] = department;
        }else {
            target = new String[1];
            target[0] = department;
        }
        company.setDepartments(target);
        companyRepository.save(company);
        logger.info("添加部门后："+company.toString());
        return true;
    }

    public List<User> getUserByDepartment(String department,
                                          User user,
                                          Company company) {
        if (user != null){
            return userRepository.findAllByDepartmentAndFromCompany_CompanyId(department,user.getFromCompany().getCompanyId());
        }else {
            return userRepository.findAllByDepartmentAndFromCompany_CompanyId(department,company.getCompanyId() );
        }
    }

    public List<User> getAllUserByCompanyId(Long companyId) {
        return userRepository.findAllByFromCompany_CompanyId(companyId);
    }

    public List<User> getAuthUser(Company company) {
        List<User> users = userRepository.findAllByFromCompany_CompanyId(company.getCompanyId());
        users = users.stream().filter(user -> {
            if (user.getAuth() == null){
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return users;
    }

    public boolean dealAuthUser(Long userId, Integer status) {
        User user = userRepository.findById(userId).get();
        if (status.equals(1)){
            user.setAuth(true);
        }else{
            user.setAuth(false);
        }
        userRepository.save(user);
        return true;
    }

    public Company login(Company company) {
        Company company1 = companyRepository.findByMobilePhone(company.getMobilePhone());
        if (company1.getPassword().equals(MD5Util.md5(company.getPassword()))){
            return company1;
        }
        return null;
    }

    public boolean uploadAvatar(String base64Avatar, Company sessionCompany) {
        sessionCompany = companyRepository.findById(sessionCompany.getCompanyId()).get();
        sessionCompany.setAvatar(UrlConstant.IMAGE_URL+new FastDfsUtil().uploadBase64Image(base64Avatar));
        return true;
    }

    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }

    public Company findPassword(String mobilePhone, String password) {
        Company company = companyRepository.findByMobilePhone(mobilePhone);
        company.setPassword(MD5Util.md5(password));
        companyRepository.save(company);
        return company;
    }
}
