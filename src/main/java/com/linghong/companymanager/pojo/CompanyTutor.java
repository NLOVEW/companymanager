package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
/**
 * @Auther: luck_nhb
 * @Date: 2018/11/6 13:56
 * @Version 1.0
 * @Description: 公司帮手 公司辅导
 */
@Entity
@Table(name = "company_tutor")
public class CompanyTutor implements Serializable {
    private String companyTutorId;
    private Company tutorCompany;
    private String title;
    private String type;
    private String message;
    private BigDecimal price;
    private Set<DiscussMessage> discussMessages;
    private Date pushTime;

    @Id
    @Column(name = "companyTutorId",unique = true,length = 32)
    public String getCompanyTutorId() {
        return companyTutorId;
    }

    public void setCompanyTutorId(String companyTutorId) {
        this.companyTutorId = companyTutorId;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "tutorCompanyId")
    public Company getTutorCompany() {
        return tutorCompany;
    }

    public void setTutorCompany(Company tutorCompany) {
        this.tutorCompany = tutorCompany;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name = "companyTutorId")
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CompanyTutor{" +
                "companyTutorId='" + companyTutorId + '\'' +
                ", tutorCompany=" + tutorCompany +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", price=" + price +
                ", discussMessages=" + discussMessages +
                ", pushTime=" + pushTime +
                '}';
    }
}
