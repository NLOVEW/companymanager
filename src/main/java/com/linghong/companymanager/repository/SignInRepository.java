package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.SignIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SignInRepository extends JpaRepository<SignIn,String> {
    List<SignIn> findAllByFromUser_UserId(@Param("userId") Long userId);
    List<SignIn> findAllByFromUser_FromCompany_CompanyId(@Param("companyId") Long companyId);
}
