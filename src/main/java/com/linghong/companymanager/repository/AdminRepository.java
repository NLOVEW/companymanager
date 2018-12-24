package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findByMobilePhone(@Param("mobilePhone") String mobilePhone);
}
