package com.linghong.companymanager.service;

import com.linghong.companymanager.pojo.*;
import com.linghong.companymanager.repository.*;
import com.linghong.companymanager.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: luck_nhb
 * @Date: 2018/12/11 15:45
 * @Version 1.0
 * @Description:
 */
@Service
public class AdminService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private UserRepository userRepository;
    @Resource
    private CompanyRepository companyRepository;
    @Resource
    private CompanyDynamicMessageRepository companyDynamicMessageRepository;
    @Resource
    private ChargeRepository chargeRepository;
    @Resource
    private CompanyTutorRepository companyTutorRepository;
    @Resource
    private CompanyTalkMsgRepository companyTalkMsgRepository;
    @Resource
    private PersonTalkMsgRepository personTalkMsgRepository;
    @Resource
    private AdminRepository adminRepository;
    @Resource
    private MessageBackRepository messageBackRepository;

    public Admin login(String mobilePhone, String password) {
        Admin admin = adminRepository.findByMobilePhone(mobilePhone);
        if (admin.getPassword().equals(MD5Util.md5(password))){
            return admin;
        }
        return null;
    }

    public Map<String, Object> index() {
        Map<String,Object> result = new HashMap<>();
        List<Company> authTrue = companyRepository.findAllByAuth(true);
        result.put("authTrue",authTrue);
        List<Company> authFalse = companyRepository.findAllByAuth(false);
        result.put("authFalse",authFalse);
        List<PersonTalkMsg> personTalkMsgs = personTalkMsgRepository.findAll();
        result.put("personTalkMsgs",personTalkMsgs);
        List<CompanyTalkMsg> companyTalkMsgs = companyTalkMsgRepository.findAll();
        result.put("companyTalkMsgs",companyTalkMsgs);
        List<CompanyTutor> companyTutors = companyTutorRepository.findAll();
        result.put("companyTutors",companyTutors);
        List<CompanyDynamicMessage> dynamicMessages = companyDynamicMessageRepository.findAll();
        result.put("dynamicMessages",dynamicMessages);
        List<MessageBack> messageBacks = messageBackRepository.findAll();
        result.put("messageBacks",messageBacks);
        return result;
    }

    public void agreeAuthCompany(Long companyId) {
        Company company = companyRepository.findById(companyId).get();
        company.setAuth(true);
        companyRepository.save(company);
    }

    public void handlerMessage(String messageBackId) {
        messageBackRepository.deleteById(messageBackId);
    }

    public void updatePassword(Admin admin) {
        adminRepository.save(admin);
    }
}
