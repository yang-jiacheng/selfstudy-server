package com.lxy.admin.controller;

import com.lxy.admin.security.service.LoginService;
import com.lxy.admin.util.CodeUtil;
import com.lxy.common.bo.R;
import com.lxy.common.constant.CommonConstants;
import com.lxy.common.util.JsonWebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/10/08 19:57
 * @Version: 1.0
 */

@RequestMapping("/token")
@Controller
public class TokenController {

    private final CodeUtil codeUtil;

    private final LoginService loginService;

    @Autowired
    public TokenController(CodeUtil codeUtil,LoginService loginService) {
        this.codeUtil = codeUtil;
        this.loginService = loginService;
    }

    @PostMapping(value = "/login", produces = "application/json")
    @ResponseBody
    public R<Object> login(@RequestParam("username")String username,
                   @RequestParam("password")String password,
                   @RequestParam("verifyCode")String verifyCode,
                   HttpServletResponse response, HttpServletRequest request){
        boolean flag = codeUtil.checkVerifyCode(verifyCode,request);
        if (!flag){
            return R.fail("验证码错误，请重新输入!");
        }
        R<Object> result = loginService.login(username, password, "web",response);
        return result;
    }

    @PostMapping(value = "/logout", produces = "application/json")
    @ResponseBody
    public R<Object> logout(HttpServletRequest request,HttpServletResponse response){
        String accessToken = JsonWebTokenUtil.getAccessToken(request, CommonConstants.COOKIE_NAME_ADMIN);
        loginService.logout(accessToken,request,response);
        return R.ok();
    }

}
