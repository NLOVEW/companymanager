package com.linghong.companymanager.service;

import com.linghong.companymanager.pojo.Charge;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.repository.ChargeRepository;
import com.linghong.companymanager.repository.UserRepository;
import com.linghong.companymanager.utils.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/7 10:07
 * @Version 1.0
 * @Description:
 */
@Service
public class ChargeService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private ChargeRepository chargeRepository;
    @Resource
    private UserRepository userRepository;

    public boolean pushCharge(Charge charge, User user, Company company) {
        if (user != null) {
            charge.setUser(user);
        } else {
            charge.setCompany(company);
        }
        charge.setCreateTime(new Date());
        charge.setChargeId(IDUtil.getId());
        charge = chargeRepository.save(charge);
        if (charge != null) {
            return true;
        }
        return false;
    }

    public List<Charge> getCurrentUserCharge(User user, Company company) {
        if (user != null) {
            return chargeRepository.findAllByUser_UserIdOrderByCreateTimeDesc(user.getUserId());
        } else {
            return chargeRepository.findAllByCompany_CompanyIdOrderByCreateTimeDesc(company.getCompanyId());
        }

    }
}
