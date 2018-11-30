package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company,Long>, JpaSpecificationExecutor<Company> {
    Company findByMobilePhone(@Param("mobilePhone") String mobilePhone);
}
