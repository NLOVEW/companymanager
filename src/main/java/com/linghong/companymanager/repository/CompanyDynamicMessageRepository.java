package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.CompanyDynamicMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyDynamicMessageRepository extends JpaRepository<CompanyDynamicMessage,String> {
    List<CompanyDynamicMessage> findAllByCompany_CompanyId(@Param("companyId") Long companyId);
}
