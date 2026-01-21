package com.lxy.admin.controller;

import cn.hutool.extra.servlet.JakartaServletUtil;
import com.lxy.admin.security.service.LoginService;
import com.lxy.common.model.R;
import com.lxy.common.model.TokenPair;
import com.lxy.system.dto.LoginVerifyCodeDTO;
import com.lxy.common.util.DualTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录认证
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/10/08 19:57
 */

@RequestMapping("/token")
@Controller
public class TokenController {

    @Resource
    private LoginService loginService;

    /**
     * 登录
     *
     * @author jiacheng yang.
     * @since 2025/03/07 14:53
     */
    @PostMapping(value = "/login", produces = "application/json")
    @ResponseBody
    public R<Object> login(@RequestBody @Valid LoginVerifyCodeDTO dto, HttpServletRequest request,
        HttpServletResponse response) {
        String clientIP = JakartaServletUtil.getClientIP(request);
        dto.setClientIp(clientIP);
        return loginService.login(dto, response);
    }

    /**
     * 退出登录
     *
     * @author jiacheng yang.
     * @since 2025/03/07 14:53
     */
    @PostMapping(value = "/logout", produces = "application/json")
    @ResponseBody
    public R<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = DualTokenUtil.getToken(request, DualTokenUtil.TOKEN_REFRESH);
        loginService.logout(accessToken, request, response);
        return R.ok();
    }

    /**
     * 刷新访问令牌
     *
     * @author jiacheng yang.
     * @since 2025/01/18 15:00
     */
    @PostMapping(value = "/refresh", produces = "application/json")
    @ResponseBody
    public R<TokenPair> refreshToken(HttpServletRequest request) {
        String refreshToken = DualTokenUtil.getToken(request, DualTokenUtil.TOKEN_REFRESH);
        String clientIp = JakartaServletUtil.getClientIP(request);
        return loginService.refreshToken(refreshToken, clientIp);
    }

}
