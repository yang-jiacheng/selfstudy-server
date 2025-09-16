package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: 用户表
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

@TableName(value = "user", autoResultMap = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //密码
    private String password;

    //姓名
    private String name;

    //手机号
    private String phone;

    //头像地址
    private String profilePath;

    //个人资料背景图
    private String coverPath;

    //性别(男，女)
    private String gender;

    //注册时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    //修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    //注册类型 1。用户注册 2.后台添加
    private Integer registType;

    //常驻地址
    private String address;

    //余额
    private BigDecimal balance;

    //总学习时长
    private Integer totalDuration;

    public User() {
    }

    public User(String name, String phone, String profilePath, String gender, Date createTime, Integer registType) {
        this.name = name;
        this.phone = phone;
        this.profilePath = profilePath;
        this.gender = gender;
        this.createTime = createTime;
        this.registType = registType;
    }

    public User(Integer id, String name, String profilePath, String coverPath, String gender, String address) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
        this.coverPath = coverPath;
        this.gender = gender;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public Integer getRegistType() {
        return registType;
    }

    public void setRegistType(Integer registType) {
        this.registType = registType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }


    @Override
    public String toString() {
        return "User{" +
                ", id=" + id +
                ", password=" + password +
                ", name=" + name +
                ", phone=" + phone +
                ", profilePath=" + profilePath +
                ", coverPath=" + coverPath +
                ", gender=" + gender +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", registType=" + registType +
                ", address=" + address +
                ", balance=" + balance +
                ", totalDuration=" + totalDuration +
                "}";
    }

}
