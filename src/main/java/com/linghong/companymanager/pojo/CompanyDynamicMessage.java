package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/6 13:51
 * @Version 1.0
 * @Description: 公司动态信息
 */
@Entity
@Table(name = "company_dynamic_message")
public class CompanyDynamicMessage implements Serializable {
    private String dynamicMessageId;
    private Company company;
    private String title;
    private String message;
    private String imagePath;
    private Set<DiscussMessage> discussMessages;
    private Boolean obtained;
    private Date pushTime;

    @Id
    @Column(name = "dynamicMessageId",unique = true,length = 32)
    public String getDynamicMessageId() {
        return dynamicMessageId;
    }

    public void setDynamicMessageId(String dynamicMessageId) {
        this.dynamicMessageId = dynamicMessageId;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "companyId")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "dynamicMessageId")
    public Set<DiscussMessage> getDiscussMessages() {
        return discussMessages;
    }

    public void setDiscussMessages(Set<DiscussMessage> discussMessages) {
        this.discussMessages = discussMessages;
    }

    public Boolean getObtained() {
        return obtained;
    }

    public void setObtained(Boolean obtained) {
        this.obtained = obtained;
    }
}
