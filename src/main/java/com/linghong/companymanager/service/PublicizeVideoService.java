package com.linghong.companymanager.service;

import com.linghong.companymanager.constant.UrlConstant;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.PublicizeVideo;
import com.linghong.companymanager.repository.PublicizeVideoRepository;
import com.linghong.companymanager.utils.FastDfsUtil;
import com.linghong.companymanager.utils.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/8 11:12
 * @Version 1.0
 * @Description:
 */
@Service
public class PublicizeVideoService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private PublicizeVideoRepository publicizeVideoRepository;

    public boolean pushVideoByCompany(Company company, MultipartFile video) {
        String url = UrlConstant.IMAGE_URL+new FastDfsUtil().uploadImage(video);
        PublicizeVideo publicizeVideo = new PublicizeVideo();
        publicizeVideo.setVideoPath(url);
        publicizeVideo.setPushTime(new Date());
        publicizeVideo.setPublicizeVideoId(IDUtil.getId());
        publicizeVideo.setFromCompany(company);
        publicizeVideo = publicizeVideoRepository.save(publicizeVideo);
        if (publicizeVideo != null){
            return true;
        }
        return false;
    }

    public List<PublicizeVideo> getCompanyVideoByCompanyId(Long companyId) {
        return publicizeVideoRepository.findAllByFromCompany_CompanyId(companyId);
    }

    public List<PublicizeVideo> getAllCompanyVideo() {
        return publicizeVideoRepository.findAll();
    }
}
