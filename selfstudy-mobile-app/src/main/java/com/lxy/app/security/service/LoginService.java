package com.lxy.app.security.service;

import com.lxy.system.dto.LoginPasswordDTO;
import com.lxy.system.dto.LoginVerificationCodeDTO;

/**
 * app登录服务
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2023/02/13 10:27
 */
public interface LoginService {

    /**
     * 密码登录
     *
     * @author jiacheng yang.
     * @since 2025/11/19 16:13
     * @return token 令牌
     */
    String login(LoginPasswordDTO dto);

    /**
     * 验证码登录
     *
     * @author jiacheng yang.
     * @since 2025/11/19 16:17
     * @param dto 验证码登录DTO
     * @return token 令牌
     */
    String loginByVerificationCode(LoginVerificationCodeDTO dto);

    void logout(String token);

}
