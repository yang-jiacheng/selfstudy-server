package com.lxy.app.controller;

import com.lxy.app.security.service.LoginService;
import com.lxy.common.constant.AuthConstant;
import com.lxy.common.domain.R;
import com.lxy.common.dto.LoginPasswordDTO;
import com.lxy.common.dto.LoginVerificationCodeDTO;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.system.service.PhoneCodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 身份认证
 *
 * @author jiacheng yang.
 * @since 2022/10/09 15:10
 * @version 1.0
 */

@RequestMapping("/token")
@RestController
public class TokenController {

    private final LoginService loginService;

    private final PhoneCodeService phoneCodeService;

    @Autowired
    public TokenController(LoginService loginService, PhoneCodeService phoneCodeService) {
        this.loginService = loginService;
        this.phoneCodeService = phoneCodeService;
    }

    /**
     * 密码登录
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:17
     */
    @PostMapping("/login")
    public R<Object> login(@RequestBody @Valid LoginPasswordDTO dto) {
        dto.setDevice(AuthConstant.DEVICE_ANDROID);
        String token = loginService.login(dto);
        return R.ok(token);
    }

    /**
     * 验证码 登录|注册
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:16
     */
    @PostMapping("/loginByVerificationCode")
    public R<Object> loginByVerificationCode(@RequestBody @Valid LoginVerificationCodeDTO dto) {
        String token = loginService.loginByVerificationCode(dto);
        return R.ok(token);
    }

    /**
     * 获取验证码
     *
     * @author jiacheng yang.
     * @since 2025/11/18 18:13
     * @param phone 手机号
     */
    @PostMapping("/getVerificationCode")
    public R<Object> getVerificationCode(@RequestParam(value = "phone") String phone) {
        phoneCodeService.sendVerificationCode(phone);
        return R.ok();
    }

    /**
     * 登出
     *
     * @author jiacheng yang.
     * @since 2025/11/19 15:56
     */
    @PostMapping("/logout")
    public R<Object> logout(HttpServletRequest request) {
        String accessToken = JsonWebTokenUtil.getAccessToken(request, AuthConstant.TOKEN_NAME_APP);
        loginService.logout(accessToken);
        return R.ok();
    }

}
