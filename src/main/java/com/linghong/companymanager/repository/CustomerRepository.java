package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    List<Customer> findAllByFromCompany_CompanyId(@Param("companyId") Long companyId);
    List<Customer> findAllByFromUser_UserId(@Param("userId") Long userId);
}
