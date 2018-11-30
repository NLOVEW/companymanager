package com.linghong.companymanager.service;

import com.linghong.companymanager.constant.UrlConstant;
import com.linghong.companymanager.pojo.*;
import com.linghong.companymanager.repository.BusinessMessageRepository;
import com.linghong.companymanager.repository.DiscussMessageRepository;
import com.linghong.companymanager.utils.FastDfsUtil;
import com.linghong.companymanager.utils.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/10 19:21
 * @Version 1.0
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessMessageService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private BusinessMessageRepository businessMessageRepository;
    @Resource
    private DiscussMessageRepository discussMessageRepository;

    /**
     * 已过期
     *
     * @param businessMessage
     * @param user
     * @param base64Images
     * @return
     * @see {@link BusinessMessageService#pushBusinessMessage(com.linghong.companymanager.pojo.BusinessMessage, com.linghong.companymanager.pojo.User, com.linghong.companymanager.pojo.Company, java.lang.String)}
     */
    @Deprecated
    public boolean pushBusinessMessage(BusinessMessage businessMessage,
                                       User user,
                                       String base64Images) {
        businessMessage.setPushTime(new Date());
        businessMessage.setBusinessMessageId(IDUtil.getId());
        if (base64Images != null) {
            String[] split = base64Images.split("。");
            Set<Image> images = new HashSet<>();
            for (String imagePath : split) {
                Image image = new Image();
                image.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(imagePath));
                image.setCreateTime(new Date());
                images.add(image);
            }
            businessMessage.setImages(images);
        }
        businessMessage.setUser(user);
        businessMessageRepository.save(businessMessage);
        return true;
    }


    /**
     * 已过期
     *
     * @param businessMessage
     * @param company
     * @param base64Images
     * @return
     * @see {@link BusinessMessageService#pushBusinessMessage(com.linghong.companymanager.pojo.BusinessMessage, com.linghong.companymanager.pojo.User, com.linghong.companymanager.pojo.Company, java.lang.String)}
     */
    @Deprecated
    public boolean pushBusinessMessage(BusinessMessage businessMessage,
                                       Company company,
                                       String base64Images) {
        businessMessage.setPushTime(new Date());
        businessMessage.setBusinessMessageId(IDUtil.getId());
        if (base64Images != null) {
            String[] split = base64Images.split("。");
            Set<Image> images = new HashSet<>();
            for (String imagePath : split) {
                Image image = new Image();
                image.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(imagePath));
                image.setCreateTime(new Date());
                images.add(image);
            }
            businessMessage.setImages(images);
        }
        businessMessage.setCompany(company);
        businessMessageRepository.save(businessMessage);
        return true;
    }

    public boolean pushBusinessMessage(BusinessMessage businessMessage,
                                       User user,
                                       Company company,
                                       String base64Images) {
        businessMessage.setPushTime(new Date());
        businessMessage.setBusinessMessageId(IDUtil.getId());
        if (base64Images != null) {
            String[] split = base64Images.split("。");
            Set<Image> images = new HashSet<>();
            for (String imagePath : split) {
                Image image = new Image();
                image.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(imagePath));
                image.setCreateTime(new Date());
                images.add(image);
            }
            businessMessage.setImages(images);
        }
        if (user != null) {
            businessMessage.setUser(user);
        } else {
            businessMessage.setCompany(company);
        }
        businessMessageRepository.save(businessMessage);
        return true;
    }

    public List<BusinessMessage> getBusinessMessagesByCompanyId(Long companyId) {
        List<BusinessMessage> list1 = businessMessageRepository.findAllByCompany_CompanyId(companyId);
        List<BusinessMessage> list2 = businessMessageRepository.findAllByUser_FromCompany_CompanyId(companyId);
        if (list1 != null && list1.size() > 0 && list2 != null && list2.size() > 0) {
            List<BusinessMessage> messageList = Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
            return messageList;
        } else if (list1 != null && list1.size() > 0) {
            return list1;
        } else {
            return list2;
        }
    }

    public BusinessMessage getBusinessMessageByBusinessMessageId(String businessMessageId) {
        BusinessMessage businessMessage = businessMessageRepository.findById(businessMessageId).get();
        return businessMessage;
    }

    public boolean pushDiscussMessage(DiscussMessage discussMessage,
                                      String businessMessageId,
                                      String baseImage,
                                      User user,
                                      Company company) {
        //判断user,company谁进行评论
        if (user != null) {
            discussMessage.setFromUser(user);
        } else {
            discussMessage.setFromCompany(company);
        }
        if (baseImage != null) {
            discussMessage.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(baseImage));
        }
        //判断是否回复评论
        if (discussMessage.getDiscussMessageId() != null) {
            DiscussMessage targetMessage = discussMessageRepository.findById(discussMessage.getDiscussMessageId())
                    .get();
            Company fromCompany = targetMessage.getFromCompany();
            User fromUser = targetMessage.getFromUser();
            //判断上一次评论是公司发布还是员工发布
            if (fromCompany != null) {
                discussMessage.setToCompany(fromCompany);
            } else {
                discussMessage.setToUser(fromUser);
            }
        }
        discussMessage.setPushTime(new Date());
        discussMessage.setDiscussMessageId(IDUtil.getId());
        BusinessMessage businessMessage = businessMessageRepository.findById(businessMessageId).get();
        Set<DiscussMessage> discussMessages = businessMessage.getDiscussMessages();
        for (DiscussMessage message : discussMessages){
            logger.info(message.getMessage());
        }
        discussMessages.add(discussMessage);
        businessMessage.setDiscussMessages(discussMessages);
        return true;
    }


}
