package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill,Long> {
    List<Bill> findAllByCompany_CompanyId(@Param("companyId") Long companyId);
    List<Bill> findAllByUser_UserId(@Param("userId") Long userId);
}
