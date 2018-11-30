package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/3 11:25
 * @Version 1.0
 * @Description: 互动   数据存储在redis中
 */
@Entity
@Table(name = "person_talk_msg")
public class PersonTalkMsg implements Serializable {
    private String personTalkMsgId;
    private User fromUser;
    private Company fromCompany;
    private String title;
    private String message;
    private BigDecimal price;
    private String imagePath;
    private Long lookNumber;
    private Long loveNumber;
    private Long discussNumber;
    private Set<DiscussMessage> discussMessages;
    private Date pushTime;

    @Id
    @Column(name = "personTalkMsgId",unique = true,length = 32)
    public String getPersonTalkMsgId() {
        return personTalkMsgId;
    }

    public void setPersonTalkMsgId(String personTalkMsgId) {
        this.personTalkMsgId = personTalkMsgId;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fromUserId")
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
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
    @JoinColumn(name = "personTalkMsgId")
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


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public Long getDiscussNumber() {
        return discussNumber;
    }

    public void setDiscussNumber(Long discussNumber) {
        this.discussNumber = discussNumber;
    }

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fromCompanyId")
    public Company getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(Company fromCompany) {
        this.fromCompany = fromCompany;
    }
}
