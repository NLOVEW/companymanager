package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.CompanyTalkMsgOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyTalkMsgOrderRepository extends JpaRepository<CompanyTalkMsgOrder,String> {
    List<CompanyTalkMsgOrder> findAllByFromUser_UserId(@Param("userId") Long userId);
    List<CompanyTalkMsgOrder> findAllByFromCompany_CompanyId(@Param("companyId") Long companyId);

    List<CompanyTalkMsgOrder> findAllByCompanyTalkMsg_FromCompany_CompanyId(@Param("companyId") Long companyId);
    List<CompanyTalkMsgOrder> findAllByCompanyTalkMsg_FromUser_UserId(@Param("userId") Long userId);
}
