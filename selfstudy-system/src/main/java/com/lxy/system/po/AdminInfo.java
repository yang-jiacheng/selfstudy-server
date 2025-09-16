package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 后台管理员表
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

public class AdminInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    //管理员自增主键
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String phone;

    //用户名
    private String username;

    //昵称
    private String name;

    //密码
    private String password;

    //头像地址
    private String profilePath;

    //修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    //状态 1正常 2禁用
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "AdminInfo{" +
                ", id=" + id +
                ", phone=" + phone +
                ", username=" + username +
                ", name=" + name +
                ", password=" + password +
                ", profilePath=" + profilePath +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", status=" + status +
                "}";
    }

}
