package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/7 09:21
 * @Version 1.0
 * @Description:
 */
@Entity
@Table(name = "message_back")
public class MessageBack implements Serializable {
    private String messageBackId;
    private User fromUser;
    private Company fromCompany;
    private String messageType;
    private String message;
    private String imagePath;
    private Date pushTime;

    @Id
    @Column(name = "messageBackId",unique = true,length = 32)
    public String getMessageBackId() {
        return messageBackId;
    }

    public void setMessageBackId(String messageBackId) {
        this.messageBackId = messageBackId;
    }

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fromUserId")
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
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
    @JoinColumn(name = "fromCompanyId")
    public Company getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(Company fromCompany) {
        this.fromCompany = fromCompany;
    }
}
