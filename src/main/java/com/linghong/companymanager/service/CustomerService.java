package com.linghong.companymanager.service;

import com.linghong.companymanager.constant.UrlConstant;
import com.linghong.companymanager.pojo.Customer;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.repository.CustomerRepository;
import com.linghong.companymanager.utils.FastDfsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/12 16:19
 * @Version 1.0
 * @Description:
 */
@Service
public class CustomerService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private CustomerRepository customerRepository;


    public boolean pushCustomer(User user,
                                Customer customer,
                                String base64Image) {
        if (base64Image != null) {
            customer.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(base64Image));
        }
        customer.setFromUser(user);
        customer.setFromCompany(user.getFromCompany());
        customer.setCreateTime(new Date());
        customerRepository.save(customer);
        return true;
    }

    public List<Customer> getCustomerByCompanyId(Long companyId) {
        return customerRepository.findAllByFromCompany_CompanyId(companyId);
    }

    public Customer getCustomerByCustomerId(Long customerId) {
        return customerRepository.findById(customerId).get();
    }

    public List<Customer> getCustomerByUserId(Long userId) {
        return customerRepository.findAllByFromUser_UserId(userId);
    }

    public boolean addStatus(Long customerId, String status) {
        Customer customer = customerRepository.findById(customerId).get();
        String[] customerStatus = customer.getStatus();
        String[] target = new String[customerStatus.length + 1];
        for (int i = 0; i < customerStatus.length; i++) {
            target[i] = customerStatus[i];
        }
        target[customerStatus.length] = status;
        customer.setStatus(target);
        return true;
    }
}
