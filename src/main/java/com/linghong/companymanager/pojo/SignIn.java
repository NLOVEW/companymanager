package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/7 09:05
 * @Version 1.0
 * @Description:  打卡
 */
@Entity
@Table(name = "sign_in")
public class SignIn implements Serializable {
    private String signInId;
    private User fromUser;
    private String day;
    private Date createTime;

    @Id
    @Column(name = "signInId",unique = true,length = 32)
    public String getSignInId() {
        return signInId;
    }

    public void setSignInId(String signInId) {
        this.signInId = signInId;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fromUserId")
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
