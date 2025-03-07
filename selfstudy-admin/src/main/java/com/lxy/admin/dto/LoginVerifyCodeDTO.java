package com.lxy.admin.dto;


import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/03/07 14:45
 */

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

    private String device  = "web";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
