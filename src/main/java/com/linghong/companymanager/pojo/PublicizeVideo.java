package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/6 16:36
 * @Version 1.0
 * @Description:
 */
@Entity
@Table(name = "publicize_video")
public class PublicizeVideo implements Serializable {
    private String publicizeVideoId;
    private Company fromCompany;
    private String title;
    private String introduce;
    private String videoPath;
    private Date pushTime;

    @Id
    @Column(name = "publicizeVideoId",unique = true,length = 32)
    public String getPublicizeVideoId() {
        return publicizeVideoId;
    }

    public void setPublicizeVideoId(String publicizeVideoId) {
        this.publicizeVideoId = publicizeVideoId;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fromCompanyId")
    public Company getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(Company fromCompany) {
        this.fromCompany = fromCompany;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
