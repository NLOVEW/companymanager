package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/3 11:34
 * @Version 1.0
 * @Description: 公司互动  存储在Redis中
 */
@Entity
@Table(name = "company_talk_msg")
public class CompanyTalkMsg implements Serializable {
    private String companyTalkMsgId;
    private Company fromCompany;
    private User fromUser;
    private String title;
    private String message;
    private String imagePath;
    private String introduce;
    private BigDecimal price;
    private Set<DiscussMessage> discussMessages;
    private Date pushTime;

    @Id
    @Column(name = "companyTalkMsgId",unique = true,length = 32)
    public String getCompanyTalkMsgId() {
        return companyTalkMsgId;
    }

    public void setCompanyTalkMsgId(String companyTalkMsgId) {
        this.companyTalkMsgId = companyTalkMsgId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name = "companyTalkMsgId")
    public Set<DiscussMessage> getDiscussMessages() {
        return discussMessages;
    }

    public void setDiscussMessages(Set<DiscussMessage> discussMessages) {
        this.discussMessages = discussMessages;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fromUserId")
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
