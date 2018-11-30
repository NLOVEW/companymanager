package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChargeRepository extends JpaRepository<Charge,String>, JpaSpecificationExecutor<Charge> {
   List<Charge> findAllByUser_UserIdOrderByCreateTimeDesc(@Param("userId") Long userId);
   List<Charge> findAllByCompany_CompanyIdOrderByCreateTimeDesc(@Param("companyId") Long companyId);
}
