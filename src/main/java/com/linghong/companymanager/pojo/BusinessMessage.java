package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/6 13:45
 * @Version 1.0
 * @Description:  行业信息
 */
@Entity
@Table(name = "business_message")
public class BusinessMessage implements Serializable {
    private String businessMessageId;
    private User user; //公司员工发布
    private Company company;//公司管理员发布
    private String title;
    private Set<Image> images;
    private String message;
    private Long lookNumber;
    private Long loveNumber;
    private Long commentNumber;
    private String tags[];
    private Set<DiscussMessage> discussMessages; //评论信息
    private Date pushTime;

    @Id
    @Column(name = "businessMessageId",unique = true,length = 32)
    public String getBusinessMessageId() {
        return businessMessageId;
    }

    public void setBusinessMessageId(String businessMessageId) {
        this.businessMessageId = businessMessageId;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "userId")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "businessMessageId")
    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Long getLookNumber() {
        return lookNumber;
    }

    public void setLookNumber(Long lookNumber) {
        this.lookNumber = lookNumber;
    }

    public Long getLoveNumber() {
        return loveNumber;
    }

    public void setLoveNumber(Long loveNumber) {
        this.loveNumber = loveNumber;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name = "businessMessageId")
    public Set<DiscussMessage> getDiscussMessages() {
        return discussMessages;
    }

    public void setDiscussMessages(Set<DiscussMessage> discussMessages) {
        this.discussMessages = discussMessages;
    }

    public Long getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Long commentNumber) {
        this.commentNumber = commentNumber;
    }
}
