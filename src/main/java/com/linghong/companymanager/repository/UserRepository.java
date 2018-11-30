package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByMobilePhone(@Param("mobilePhone") String mobilePhone);
    List<User> findAllByFromCompany_CompanyId(@Param("companyId") Long companyId);
    List<User> findAllByDepartmentAndFromCompany_CompanyId(@Param("department") String department,
                                                           @Param("companyId") Long companyId);
}
