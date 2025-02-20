package com.lxy.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.admin.security.service.LoginService;
import com.lxy.admin.util.CodeUtil;
import com.lxy.common.constant.CommonConstants;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.common.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.Cookie;
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
    public String login(@RequestParam("username")String username,
                        @RequestParam("password")String password,
                       @RequestParam("verifyCode")String verifyCode,
                        HttpServletResponse response, HttpServletRequest request){
        boolean flag = codeUtil.checkVerifyCode(verifyCode,request);
        if (!flag){
            return JsonUtil.toJson(new ResultVO(-1,"验证码错误，请重新输入!"));
        }
        ResultVO result = loginService.login(username, password, "web",response);
        return JsonUtil.toJson(result);
    }

    @PostMapping(value = "/logout", produces = "application/json")
    @ResponseBody
    public String logout(HttpServletRequest request,HttpServletResponse response){
        String accessToken = JsonWebTokenUtil.getAccessToken(request, CommonConstants.COOKIE_NAME_ADMIN);
        loginService.logout(accessToken,request,response);
        return JsonUtil.toJson(new ResultVO());
    }

}
