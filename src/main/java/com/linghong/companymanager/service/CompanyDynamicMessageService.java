package com.linghong.companymanager.service;

import com.linghong.companymanager.constant.UrlConstant;
import com.linghong.companymanager.pojo.*;
import com.linghong.companymanager.repository.CompanyDynamicMessageRepository;
import com.linghong.companymanager.repository.DiscussMessageRepository;
import com.linghong.companymanager.utils.FastDfsUtil;
import com.linghong.companymanager.utils.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/12 16:55
 * @Version 1.0
 * @Description:
 */
@Service
public class CompanyDynamicMessageService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private CompanyDynamicMessageRepository companyDynamicMessageRepository;
    @Resource
    private DiscussMessageRepository discussMessageRepository;

    public boolean pushCompanyDynamicMessage(CompanyDynamicMessage companyDynamicMessage,
                                             String base64Image,
                                             Company company) {
        if (base64Image != null){
            companyDynamicMessage.setImagePath(UrlConstant.IMAGE_URL+new FastDfsUtil().uploadBase64Image(base64Image));
        }
        companyDynamicMessage.setObtained(false);
        companyDynamicMessage.setCompany(company);
        companyDynamicMessage.setDynamicMessageId(IDUtil.getId());
        companyDynamicMessage.setPushTime(new Date());
        companyDynamicMessageRepository.save(companyDynamicMessage);
        return true;
    }

    public List<CompanyDynamicMessage> getCompanyDynamicMessageByCompanyId(Long companyId) {
        return companyDynamicMessageRepository.findAllByCompany_CompanyId(companyId);
    }

    public CompanyDynamicMessage getCompanyDynamicMessageByDynamicMessageId(String dynamicMessageId) {
        return companyDynamicMessageRepository.findById(dynamicMessageId).get();
    }

    public boolean pushDiscussMessage(DiscussMessage discussMessage,
                                      String dynamicMessageId,
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
        CompanyDynamicMessage dynamicMessage = companyDynamicMessageRepository.findById(dynamicMessageId).get();
        Set<DiscussMessage> discussMessages = dynamicMessage.getDiscussMessages();
        discussMessages.add(discussMessage);
        return true;
    }

    public boolean deleteCompanyDynamicMessage(String dynamicMessageId) {
        CompanyDynamicMessage dynamicMessage = companyDynamicMessageRepository.findById(dynamicMessageId).get();
        dynamicMessage.setObtained(true);
        return true;
    }
}
