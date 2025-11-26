package com.lxy.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 管理员信息修改DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/11/26 10:17
 */

@Data
public class AdminInfoUpdateDTO {

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

    // 状态 1正常 2禁用
    private Integer status;

}
