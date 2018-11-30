package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.CompanyTalkMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyTalkMsgRepository extends JpaRepository<CompanyTalkMsg,String> {
    List<CompanyTalkMsg> findAllByFromCompany_CompanyId(@Param("companyId") Long companyId);
    List<CompanyTalkMsg> findAllByFromUser_FromCompany_CompanyId(@Param("companyId") Long companyId);
    List<CompanyTalkMsg> findAllByFromUser_UserId(@Param("userId") Long userId);
}
