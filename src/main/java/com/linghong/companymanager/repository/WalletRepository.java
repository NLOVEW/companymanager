package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface WalletRepository extends JpaRepository<Wallet,String> {
    Wallet findByUser_UserId(@Param("userId") Long userId);
    Wallet findByCompany_CompanyId(@Param("companyId") Long companyId);
}
