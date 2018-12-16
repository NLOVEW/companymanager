package com.linghong.companymanager.service;

import com.linghong.companymanager.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
