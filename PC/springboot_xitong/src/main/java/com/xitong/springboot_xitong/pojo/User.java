package com.xitong.springboot_xitong.pojo;

import jakarta.persistence.*;

@Table(name="tb_user")
@Entity
public class User {
    @Id        //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer UserID;
    private String userName;       //用户名
    private String password;
    private String realName;
    private String phoneNumber;
    private String email;
    private Integer RoleID;
    //get,set,tostring方法的实现
    public Integer getUserID() {
        return UserID;
    }

    public void setUserID(Integer userID) {
        UserID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRoleID() {
        return RoleID;
    }

    public void setRoleID(Integer roleID) {
        RoleID = roleID;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserID=" + UserID +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", realName='" + realName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", RoleID=" + RoleID +
                '}';
    }
}
