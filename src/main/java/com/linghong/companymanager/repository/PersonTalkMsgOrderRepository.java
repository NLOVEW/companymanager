package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.PersonTalkMsgOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonTalkMsgOrderRepository extends JpaRepository<PersonTalkMsgOrder,String> {
    List<PersonTalkMsgOrder> findAllByFromUser_UserId(@Param("userId") Long userId);
    List<PersonTalkMsgOrder> findAllByFromCompany_CompanyId(@Param("companyId") Long companyId);

    List<PersonTalkMsgOrder> findAllByPersonTalkMsg_FromUser_UserId(@Param("userId") Long userId);
    List<PersonTalkMsgOrder> findAllByPersonTalkMsg_FromCompany_CompanyId(@Param("companyId") Long companyId);
}
