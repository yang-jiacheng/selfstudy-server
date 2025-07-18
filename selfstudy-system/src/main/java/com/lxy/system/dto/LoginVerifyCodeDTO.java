package com.lxy.system.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/03/07 14:45
 */

@Data
public class LoginVerifyCodeDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7408311634569283764L;

    @NotNull(message = "请输入用户名")
    private String username;

    @NotNull(message = "请输入密码")
    private String password;

    @NotNull(message = "请输入验证码")
    private String verifyCode;

    private String uuid;

    private String clientIp;
}
