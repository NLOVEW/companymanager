package com.linghong.companymanager.service;

import com.linghong.companymanager.pojo.Announcement;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.repository.AnnouncementRepository;
import com.linghong.companymanager.utils.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/12 11:24
 * @Version 1.0
 * @Description:
 */
@Service
public class AnnouncementService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private AnnouncementRepository announcementRepository;

    public boolean pushAnnouncement(Announcement announcement, Company company) {
        announcement.setAnnouncementId(IDUtil.getId());
        announcement.setFromCompany(company);
        announcement.setPushTime(new Date());
        announcementRepository.save(announcement);
        return true;
    }

    public List<Announcement> getAnnouncementByCompanyId(Long companyId) {
        List<Announcement> announcements = announcementRepository.findAllByFromCompany_CompanyIdOrderByPushTimeDesc(companyId);
        return announcements;
    }

    public Announcement getAnnouncementByAnnouncementId(String announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId).get();
        return announcement;
    }
}
