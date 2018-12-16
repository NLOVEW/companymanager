package com.linghong.companymanager.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/2 17:56
 * @Version 1.0
 * @Description: 用户类
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
    private Long userId;     //主键Id
    private String userName; //用户名
    private String nickName; //昵称
    private String mobilePhone;//手机号
    private String password;//密码
    private String email;//邮箱
    private String avatar;//头像
    private String sex;//性别
    private String address;
    private String sign;
    private String id; //此ID可用可不用
    private Date workTime;//在职时间
    private Boolean status;//是否在职
    private String department;
    private String position; //职位
    private String idCardNumber;//身份证号
    private String idCardPath;//身份证照片
    private String businessTarget;
    private Boolean auth;//是否被认证
    private Company fromCompany;
    private OpenUser openUser;
    private Date createTime;

    @Id
    @GeneratedValue
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getIdCardPath() {
        return idCardPath;
    }

    public void setIdCardPath(String idCardPath) {
        this.idCardPath = idCardPath;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    public Date getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Date workTime) {
        this.workTime = workTime;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fromCompanyId")
    public Company getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(Company fromCompany) {
        this.fromCompany = fromCompany;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "openUserId")
    public OpenUser getOpenUser() {
        return openUser;
    }

    public void setOpenUser(OpenUser openUser) {
        this.openUser = openUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBusinessTarget() {
        return businessTarget;
    }

    public void setBusinessTarget(String businessTarget) {
        this.businessTarget = businessTarget;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", sign='" + sign + '\'' +
                ", id='" + id + '\'' +
                ", workTime=" + workTime +
                ", status=" + status +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", idCardPath='" + idCardPath + '\'' +
                ", businessTarget='" + businessTarget + '\'' +
                ", auth=" + auth +
                ", fromCompany=" + fromCompany +
                ", openUser=" + openUser +
                ", createTime=" + createTime +
                '}';
    }
}
