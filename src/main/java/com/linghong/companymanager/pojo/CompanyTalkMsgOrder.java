package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/12 10:07
 * @Version 1.0
 * @Description: 公司互动订单
 */

@Entity
@Table(name = "company_talk_msg_order")
public class CompanyTalkMsgOrder implements Serializable {
    private String companyTalkMsgOrderId;
    private CompanyTalkMsg companyTalkMsg;
    private User fromUser;
    private Company fromCompany;
    private Integer status;//0报名并等待确认  1同意报名  2拒绝报名
    private Set<DiscussMessage> discussMessages;
    private Date pushTime;

    @Id
    @Column(name = "companyTalkMsgOrderId",unique = true,length = 32)
    public String getCompanyTalkMsgOrderId() {
        return companyTalkMsgOrderId;
    }

    public void setCompanyTalkMsgOrderId(String companyTalkMsgOrderId) {
        this.companyTalkMsgOrderId = companyTalkMsgOrderId;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "companyTalkMsgId")
    public CompanyTalkMsg getCompanyTalkMsg() {
        return companyTalkMsg;
    }

    public void setCompanyTalkMsg(CompanyTalkMsg companyTalkMsg) {
        this.companyTalkMsg = companyTalkMsg;
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
    @JoinColumn(name = "companyTalkMsgOrderId")
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
