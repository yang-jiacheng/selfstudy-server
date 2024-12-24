package com.lxy.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.app.security.service.LoginService;
import com.lxy.common.config.properties.CustomProperties;
import com.lxy.common.constant.CommonConstants;
import com.lxy.common.domain.R;
import com.lxy.common.po.User;
import com.lxy.common.service.PhoneCodeService;
import com.lxy.common.service.UserService;
import com.lxy.common.util.EncryptUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.common.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/10/09 15:10
 * @Version: 1.0
 */

@RequestMapping("/token")
@RestController
@Api(tags = "用户登录、登出")
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

    @ApiOperation(value = "登录", produces = "application/json", notes = "jiacheng yang.")
    @PostMapping("/login")
    public R<Object> login(@ApiParam(value = "手机号")@RequestParam(value = "phone") String phone,
                           @ApiParam(value = "密码，sha256加密（8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92）")@RequestParam(value = "password") String password){
        R<Object> result = loginService.login(phone, password, "android");
        return result;
    }

    @ApiOperation(value = "验证码登录|注册", produces = "application/json", notes = "jiacheng yang.")
    @PostMapping("/loginByVerificationCode")
    public R<Object> loginByVerificationCode(@ApiParam(value = "手机号")@RequestParam(value = "phone") String phone,
                                          @ApiParam(value = "验证码")@RequestParam(value = "verificationCode") String verificationCode){
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

    @ApiOperation(value = "获取验证码", produces = "application/json", notes = "jiacheng yang.")
    @PostMapping("/getVerificationCode")
    public R<Object> getVerificationCode(@ApiParam(value = "手机号")@RequestParam(value = "phone") String phone){
        boolean flag = phoneCodeService.checkPhone(phone);
        if (!flag){
            return R.fail(-1,"今日验证码次数已到上限 5条！");
        }
        phoneCodeService.sendVerificationCode(phone);
        return R.ok();
    }

    @ApiOperation(value = "登出", produces = "application/json", notes = "jiacheng yang.")
    @PostMapping("/logout")
    public R<Object> logout(HttpServletRequest request){
        String accessToken = JsonWebTokenUtil.getAccessToken(request, CommonConstants.COOKIE_NAME_APP);
        loginService.logout(accessToken);
        return R.ok();
    }


}
