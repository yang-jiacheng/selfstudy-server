package com.lxy.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 后台用户vo
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/7/16 19:25
 */

@Data
public class AdminInfoVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6034862826429517648L;

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

    private List<String> permissions;

}
