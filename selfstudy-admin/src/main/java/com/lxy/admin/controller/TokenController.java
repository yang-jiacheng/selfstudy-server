package com.lxy.admin.controller;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.lxy.admin.security.service.impl.DualTokenLoginServiceImpl;
import com.lxy.system.dto.LoginVerifyCodeDTO;
import com.lxy.admin.security.service.LoginService;
import com.lxy.common.domain.R;
import com.lxy.common.constant.CommonConstant;
import com.lxy.common.domain.TokenPair;
import com.lxy.common.util.DualTokenUtil;
import com.lxy.common.util.JsonWebTokenUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 登录认证
 * @author jiacheng yang.
 * @since 2022/10/08 19:57
 * @version 1.0
 */

@RequestMapping("/token")
@Controller
public class TokenController {

    @Resource
    private DualTokenLoginServiceImpl loginService;

    /**
     * 登录
     * @author jiacheng yang.
     * @since 2025/03/07 14:53
     */
    @PostMapping(value = "/login", produces = "application/json")
    @ResponseBody
    public R<Object> login(@RequestBody @Valid LoginVerifyCodeDTO dto, HttpServletRequest request,HttpServletResponse response){
        String clientIP = JakartaServletUtil.getClientIP(request);
        dto.setClientIp(clientIP);
        return loginService.login(dto,response);
    }

    /**
     * 退出登录
     * @author jiacheng yang.
     * @since 2025/03/07 14:53
     */
    @PostMapping(value = "/logout", produces = "application/json")
    @ResponseBody
    public R<Object> logout(HttpServletRequest request,HttpServletResponse response){
        String accessToken = DualTokenUtil.getToken(request, DualTokenUtil.TOKEN_REFRESH);
        loginService.logout(accessToken,request,response);
        return R.ok();
    }

    /**
     * 刷新访问令牌
     * @author jiacheng yang.
     * @since 2025/01/18 15:00
     */
    @PostMapping(value = "/refresh", produces = "application/json")
    @ResponseBody
    public R<TokenPair> refreshToken(HttpServletRequest request) {
        String refreshToken = DualTokenUtil.getToken(request, DualTokenUtil.TOKEN_REFRESH);
        return loginService.refreshToken(refreshToken);
    }

}
