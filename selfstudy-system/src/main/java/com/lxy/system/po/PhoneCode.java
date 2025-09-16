package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 验证码
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

@TableName(value = "phone_code", autoResultMap = true)
public class PhoneCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String phone;

    private String code;

    //1已使用
    private Integer useStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    //过期时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date endTime;

    public PhoneCode() {
    }

    public PhoneCode(String phone, String code, Integer useStatus, Date createTime, Date endTime) {
        this.phone = phone;
        this.code = code;
        this.useStatus = useStatus;
        this.createTime = createTime;
        this.endTime = endTime;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        return "PhoneCode{" +
                ", id=" + id +
                ", phone=" + phone +
                ", code=" + code +
                ", useStatus=" + useStatus +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                "}";
    }

}
