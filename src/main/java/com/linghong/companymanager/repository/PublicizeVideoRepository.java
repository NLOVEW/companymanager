package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.PublicizeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublicizeVideoRepository extends JpaRepository<PublicizeVideo,String> {
    List<PublicizeVideo> findAllByFromCompany_CompanyId(@Param("companyId") Long companyId);
}
