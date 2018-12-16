package com.linghong.companymanager.service;

import com.linghong.companymanager.constant.UrlConstant;
import com.linghong.companymanager.pojo.Company;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    public boolean pushCustomer(Company company,
                                User user,
                                Customer customer,
                                String state,
                                String base64Image) {
        logger.info("base64:"+base64Image);
        if (base64Image != null) {
            customer.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(base64Image));
        }
        customer.setFromUser(user);
        if (company != null){
            customer.setFromCompany(company);
        }else {
            customer.setFromUser(user);
            customer.setFromCompany(user.getFromCompany());
        }
        String [] status = new String[1];
        status[0] = state;
        customer.setStatus(status);
        customer.setCreateTime(new Date());
        customerRepository.save(customer);
        return true;
    }

    public List<Customer> getCustomerByCompanyId(Long companyId) {
        List<Customer> customers1 = customerRepository.findAllByFromUser_FromCompany_CompanyId(companyId);
        List<Customer> customers2 = customerRepository.findAllByFromCompany_CompanyId(companyId);
        List<Customer> collect = Stream.concat(customers1.stream(), customers2.stream()).collect(Collectors.toList());
        return collect;
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
        String[] target = null;
        if (customerStatus != null && customerStatus.length > 0){
            target = new String[customerStatus.length + 1];
            for (int i = 0; i < customerStatus.length; i++) {
                target[i] = customerStatus[i];
            }
        }else {
            target = new String[1];
            target[0] = status;
        }
        customer.setStatus(target);
        customerRepository.save(customer);
        return true;
    }
}
