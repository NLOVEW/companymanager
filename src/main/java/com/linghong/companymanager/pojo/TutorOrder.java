package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/6 16:40
 * @Version 1.0
 * @Description:
 */
@Entity
@Table(name = "tutor_order")
public class TutorOrder implements Serializable {
    private String tutorOrderId;
    private CompanyTutor tutor;
    private Company toCompany;
    private User toUser;
    private Set<DiscussMessage> discussMessages;
    private Date createTime;

    @Id
    @Column(name = "tutorOrderId",unique = true,length = 32)
    public String getTutorOrderId() {
        return tutorOrderId;
    }

    public void setTutorOrderId(String tutorOrderId) {
        this.tutorOrderId = tutorOrderId;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "companyTutorId")
    public CompanyTutor getTutor() {
        return tutor;
    }

    public void setTutor(CompanyTutor tutor) {
        this.tutor = tutor;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "toCompanyId")
    public Company getToCompany() {
        return toCompany;
    }

    public void setToCompany(Company toCompany) {
        this.toCompany = toCompany;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "toUserId")
    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name = "tutorOrderId")
    public Set<DiscussMessage> getDiscussMessages() {
        return discussMessages;
    }

    public void setDiscussMessages(Set<DiscussMessage> discussMessages) {
        this.discussMessages = discussMessages;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
