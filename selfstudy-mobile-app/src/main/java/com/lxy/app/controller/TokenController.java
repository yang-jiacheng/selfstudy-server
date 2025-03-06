package com.lxy.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.app.security.service.LoginService;
import com.lxy.common.constant.CommonConstants;
import com.lxy.common.bo.R;
import com.lxy.common.po.User;
import com.lxy.common.service.PhoneCodeService;
import com.lxy.common.service.UserService;
import com.lxy.common.util.EncryptUtil;
import com.lxy.common.util.JsonWebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Description: 身份认证
 * author: jiacheng yang.
 * Date: 2022/10/09 15:10
 * Version: 1.0
 */

@RequestMapping("/token")
@RestController
public class TokenController {

    private final UserService userService;

    private final LoginService loginService;

    private final PhoneCodeService phoneCodeService;

    @Autowired
    public TokenController(UserService userService,LoginService loginService,PhoneCodeService phoneCodeService) {
        this.userService = userService;
        this.loginService = loginService;
        this.phoneCodeService = phoneCodeService;
    }

    /**
     * Description: 密码登录
     * author: jiacheng yang.
     * Date: 2025/02/20 10:17
     * Param: [phone 手机号, password 密码，sha256加密（8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92）]
     */
    @PostMapping("/login")
    public R<Object> login(@RequestParam(value = "phone") String phone, @RequestParam(value = "password") String password){
        R<Object> result = loginService.login(phone, password, "android");
        return result;
    }

    /**
     * Description: 验证码 登录|注册
     * author: jiacheng yang.
     * Date: 2025/02/20 10:16
     * Param: [phone 手机号, verificationCode 验证码]
     */
    @PostMapping("/loginByVerificationCode")
    public R<Object> loginByVerificationCode(@RequestParam(value = "phone") String phone,
                                          @RequestParam(value = "verificationCode") String verificationCode){
        if (!"111111".equals(verificationCode)){
            boolean flag = phoneCodeService.checkVerificationCode(phone, verificationCode);
            if (! flag){
                return R.fail(-1,"验证码错误或已失效，请重新获取！");
            }
            //修改验证码为已使用
            phoneCodeService.updateVerificationCodeStatus(phone,verificationCode);
        }

        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (user == null){
            String defPath = "/upload/defPath.jpg";
            String gender = "男";
            user = new User(phone,phone,defPath,gender,new Date(),1);
            user.setPassword(EncryptUtil.encryptSha256("123"));
            userService.save(user);
        }
        R<Object> result  = loginService.login(phone,user.getPassword(),"android");
        return result;
    }

    /**
     * Description: 获取验证码
     * author: jiacheng yang.
     * Date: 2025/02/20 10:11
     * Param: [phone 手机号]
     */
    @PostMapping("/getVerificationCode")
    public R<Object> getVerificationCode(@RequestParam(value = "phone") String phone){
        boolean flag = phoneCodeService.checkPhone(phone);
        if (!flag){
            return R.fail(-1,"今日验证码次数已到上限 5条！");
        }
        phoneCodeService.sendVerificationCode(phone);
        return R.ok();
    }

    /**
     * Description: 登出
     * author: jiacheng yang.
     * Date: 2025/02/20 10:16
     * Param: [request]
     */
    @PostMapping("/logout")
    public R<Object> logout(HttpServletRequest request){
        String accessToken = JsonWebTokenUtil.getAccessToken(request, CommonConstants.COOKIE_NAME_APP);
        loginService.logout(accessToken);
        return R.ok();
    }


}
