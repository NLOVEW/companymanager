package com.linghong.companymanager.service;

import com.linghong.companymanager.constant.UrlConstant;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.MessageBack;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.repository.MessageBackRepository;
import com.linghong.companymanager.utils.FastDfsUtil;
import com.linghong.companymanager.utils.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/13 10:26
 * @Version 1.0
 * @Description:
 */

@Service
public class MessageBackService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private MessageBackRepository messageBackRepository;

    public boolean pushMessageBack(MessageBack messageBack,String base64, User user, Company company) {
        messageBack.setMessageBackId(IDUtil.getId());
        if (base64!= null){
            messageBack.setImagePath(UrlConstant.IMAGE_URL+new FastDfsUtil().uploadBase64Image(base64));
        }
        if (user != null){
            messageBack.setFromUser(user);
        }else {
            messageBack.setFromCompany(company);
        }
        messageBackRepository.save(messageBack);
        return true;
    }
}
