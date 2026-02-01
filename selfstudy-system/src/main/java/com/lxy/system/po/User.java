package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.util.DateCusUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表实体
 *
 * @author jiacheng yang.
 * @since 2025-05-26
 */

@NoArgsConstructor
@Data
@TableName(value = "user", autoResultMap = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 密码
    private String password;

    // 姓名
    private String name;

    // 手机号
    private String phone;

    // 头像地址
    private String profilePath;

    // 个人资料背景图
    private String coverPath;

    // 性别(男，女)
    private String gender;

    // 注册时间
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date createTime;

    // 修改时间
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    // 注册类型 ，字典：`register_type`
    private String registerType;

    // 常驻地址
    private String address;

    // 余额
    private BigDecimal balance;

    // 总学习时长
    private Integer totalDuration;

    public User(String name, String phone, String profilePath, String coverPath, String gender, Date createTime,
        String registerType) {
        this.name = name;
        this.phone = phone;
        this.profilePath = profilePath;
        this.coverPath = coverPath;
        this.gender = gender;
        this.createTime = createTime;
        this.registerType = registerType;
    }

    public User(Long id, String name, String profilePath, String coverPath, String gender, String address) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
        this.coverPath = coverPath;
        this.gender = gender;
        this.address = address;
    }

}
