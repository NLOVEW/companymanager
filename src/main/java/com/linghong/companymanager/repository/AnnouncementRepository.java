package com.linghong.companymanager.repository;

import com.linghong.companymanager.pojo.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement,String> {
    List<Announcement> findAllByFromCompany_CompanyIdOrderByPushTimeDesc(@Param("companyId") Long companyId);
}
