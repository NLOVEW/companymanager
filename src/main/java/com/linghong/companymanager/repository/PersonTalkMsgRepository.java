package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.PersonTalkMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonTalkMsgRepository extends JpaRepository<PersonTalkMsg,String> {
    List<PersonTalkMsg> findAllByFromCompany_CompanyId(@Param("companyId") Long companyId);
    List<PersonTalkMsg> findAllByFromUser_FromCompany_CompanyId(@Param("companyId") Long companyId);

    List<PersonTalkMsg> findAllByFromUser_UserId(@Param("userId") Long userId);
}
