package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/6 14:21
 * @Version 1.0
 * @Description:
 */
@Entity
@Table(name = "discuss_message")
public class DiscussMessage implements Serializable {
    private String discussMessageId;
    private Company fromCompany;
    private Company toCompany;
    private User fromUser;
    private User toUser;
    private String message;
    private DiscussMessage discussMessage;
    private String imagePath;
    private Date pushTime;

    @Id
    @Column(name = "discussMessageId",unique = true,length = 32)
    public String getDiscussMessageId() {
        return discussMessageId;
    }

    public void setDiscussMessageId(String discussMessageId) {
        this.discussMessageId = discussMessageId;
    }

    @OneToOne(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinColumn(name = "fromCompanyId")
    public Company getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(Company fromCompany) {
        this.fromCompany = fromCompany;
    }

    @OneToOne(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinColumn(name = "toCompanyId")
    public Company getToCompany() {
        return toCompany;
    }

    public void setToCompany(Company toCompany) {
        this.toCompany = toCompany;
    }

    @OneToOne(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinColumn(name = "fromUserId")
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    @OneToOne(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinColumn(name = "toUserId")
    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
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

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fromDiscussMessageId")
    public DiscussMessage getDiscussMessage() {
        return discussMessage;
    }

    public void setDiscussMessage(DiscussMessage discussMessage) {
        this.discussMessage = discussMessage;
    }

    @Override
    public String toString() {
        return "DiscussMessage{" +
                "discussMessageId='" + discussMessageId + '\'' +
                ", fromCompany=" + fromCompany +
                ", fromUser=" + fromUser +
                ", message='" + message + '\'' +
                ", discussMessage=" + discussMessage +
                ", imagePath='" + imagePath + '\'' +
                ", pushTime=" + pushTime +
                '}';
    }
}
