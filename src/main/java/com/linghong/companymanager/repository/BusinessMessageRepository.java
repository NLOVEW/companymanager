package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.BusinessMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusinessMessageRepository extends JpaRepository<BusinessMessage,String> {
    List<BusinessMessage> findAllByCompany_CompanyId(@Param("companyId") Long companyId);
    List<BusinessMessage> findAllByUser_FromCompany_CompanyId(@Param("companyId") Long companyId);
}
