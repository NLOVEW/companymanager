package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.CompanyTutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

public interface CompanyTutorRepository extends JpaRepository<CompanyTutor,String> , JpaSpecificationExecutor<CompanyTutor> {
    CompanyTutor findAllByTutorCompany_CompanyId(@Param("companyId") Long companyId);
}
