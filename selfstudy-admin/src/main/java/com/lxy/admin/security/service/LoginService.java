package com.lxy.admin.security.service;


import com.lxy.system.dto.LoginVerifyCodeDTO;
import com.lxy.common.domain.R;
import com.lxy.common.domain.TokenPair;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2023/02/13 10:27
 * @version 1.0
 */
public interface LoginService {

    R<Object> login(LoginVerifyCodeDTO dto, HttpServletResponse response);

    void logout(String token, HttpServletRequest request,HttpServletResponse response);

    /**
     * 刷新访问令牌
     * @param refreshToken 刷新令牌
     * @return 新的令牌对
     */
    R<TokenPair> refreshToken(String refreshToken);

}
