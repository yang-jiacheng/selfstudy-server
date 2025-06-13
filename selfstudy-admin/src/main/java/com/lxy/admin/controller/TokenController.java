package com.lxy.admin.controller;

import com.lxy.system.dto.LoginVerifyCodeDTO;
import com.lxy.admin.security.service.LoginService;
import com.lxy.common.domain.R;
import com.lxy.common.constant.CommonConstant;
import com.lxy.common.util.JsonWebTokenUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/10/08 19:57
 * @version 1.0
 */

@RequestMapping("/token")
@Controller
public class TokenController {

    @Resource
    private LoginService loginService;

    /**
     * 登录
     * @author jiacheng yang.
     * @since 2025/03/07 14:53
     */
    @PostMapping(value = "/login", produces = "application/json")
    @ResponseBody
    public R<Object> login(@RequestBody @Valid LoginVerifyCodeDTO dto, HttpServletResponse response){
        R<Object> result = loginService.login(dto,response);
        return result;
    }

    @PostMapping(value = "/logout", produces = "application/json")
    @ResponseBody
    public R<Object> logout(HttpServletRequest request,HttpServletResponse response){
        String accessToken = JsonWebTokenUtil.getAccessToken(request, CommonConstant.COOKIE_NAME_ADMIN);
        loginService.logout(accessToken,request,response);
        return R.ok();
    }

}
