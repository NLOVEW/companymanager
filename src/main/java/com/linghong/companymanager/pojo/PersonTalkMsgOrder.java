package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/11 10:55
 * @Version 1.0
 * @Description: 人脉活动订单
 */
@Entity
@Table(name = "person_talk_msg_order")
public class PersonTalkMsgOrder implements Serializable {
    private String personTalkMsgOrderId;
    private PersonTalkMsg personTalkMsg;
    private User fromUser;
    private Company fromCompany;
    private Integer status;//0报名并等待确认  1同意报名  2拒绝报名
    private Set<DiscussMessage> discussMessages;
    private Date pushTime;

    @Id
    @Column(name = "personTalkMsgOrderId", length = 32, unique = true)
    public String getPersonTalkMsgOrderId() {
        return personTalkMsgOrderId;
    }

    public void setPersonTalkMsgOrderId(String personTalkMsgOrderId) {
        this.personTalkMsgOrderId = personTalkMsgOrderId;
    }

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "personTalkMsgId")
    public PersonTalkMsg getPersonTalkMsg() {
        return personTalkMsg;
    }

    public void setPersonTalkMsg(PersonTalkMsg personTalkMsg) {
        this.personTalkMsg = personTalkMsg;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fromUserId")
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fromCompanyId")
    public Company getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(Company fromCompany) {
        this.fromCompany = fromCompany;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "personTalkMsgOrderId")
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
}
