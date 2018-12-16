package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/3 11:36
 * @Version 1.0
 * @Description:  公司信息不需要性别
 */
@Entity
@Table(name = "company")
public class Company implements Serializable {
    private Long companyId;
    private String companyName;   //公司名称
    private String mobilePhone;   //手机号
    private String password;      //密码
    private String avatar;//头像
    private String userName;      //法人代表
    private String idCardNumber;  //法人代表身份证号
    private String businessScope; //营业范围
    private String companyScope;  //公司范围
    private String companyType;   //公司类型
    private String businessLicense;//公司营业执照
    private String email;//公司邮箱
    private String address;//详细地址
    private String zipCode; //邮政编码
    private String telephone;//公司联系电话
    private String introduce;//公司简介
    private String[] departments;//部门名称
    private Boolean auth;//是否认证
    private Set<Image> images;//公司图片
    private Date createTime;//创建时间
    private Date updateTime; //信息更新时间

    @Id
    @GeneratedValue
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getCompanyScope() {
        return companyScope;
    }

    public void setCompanyScope(String companyScope) {
        this.companyScope = companyScope;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    @OneToMany(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinColumn(name = "companyId")
    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String[] getDepartments() {
        return departments;
    }

    public void setDepartments(String[] departments) {
        this.departments = departments;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userName='" + userName + '\'' +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", businessScope='" + businessScope + '\'' +
                ", companyScope='" + companyScope + '\'' +
                ", companyType='" + companyType + '\'' +
                ", businessLicense='" + businessLicense + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", telephone='" + telephone + '\'' +
                ", introduce='" + introduce + '\'' +
                ", departments=" + Arrays.toString(departments) +
                ", auth=" + auth +
                ", images=" + images +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
