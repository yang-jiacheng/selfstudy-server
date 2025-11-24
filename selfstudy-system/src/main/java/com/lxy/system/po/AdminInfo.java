package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 后台管理员表
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@TableName(value = "admin_info", autoResultMap = true)
public class AdminInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String phone;

    // 用户名
    private String username;

    // 昵称
    private String name;

    // 密码
    private String password;

    // 头像地址
    private String profilePath;

    // 修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    // 状态 1正常 2禁用
    private Integer status;

}
