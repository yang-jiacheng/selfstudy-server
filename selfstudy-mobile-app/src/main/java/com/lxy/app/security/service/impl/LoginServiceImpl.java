package com.lxy.app.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.app.security.service.LoginService;
import com.lxy.common.constant.AuthConstant;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.constant.SmsConstant;
import com.lxy.common.constant.UserConstant;
import com.lxy.common.constant.dict.RegisterTypeConstant;
import com.lxy.system.dto.LoginPasswordDTO;
import com.lxy.system.dto.LoginVerificationCodeDTO;
import com.lxy.common.exception.ServiceException;
import com.lxy.common.util.EncryptUtil;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.framework.security.domain.StatelessUser;
import com.lxy.framework.security.service.LoginStatusService;
import com.lxy.system.po.User;
import com.lxy.system.service.BusinessConfigService;
import com.lxy.system.service.PhoneCodeService;
import com.lxy.system.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * app登录服务 实现类
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2023/02/22 17:54
 */

@Service
public class LoginServiceImpl implements LoginService {

    private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private LoginStatusService loginStatusService;
    @Resource
    private BusinessConfigService businessConfigService;
    @Resource
    private PhoneCodeService phoneCodeService;
    @Resource
    private UserService userService;

    @Override
    public String login(LoginPasswordDTO dto) {
        String password = dto.getPassword();
        String phone = dto.getPhone();
        // AuthenticationManager authenticate进行用户认证
        // 会去调用UserDetailsService.loadUserByUsername方法。UserDetailsServiceImpl 实现了 UserDetailsService接口的
        // loadUserByUsername方法
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(phone, password);
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            throw new ServiceException("用户名或密码错误！", e);
        }
        StatelessUser principal = (StatelessUser)authenticate.getPrincipal();
        // 如果认证通过了，使用userid生成一个jwt jwt存入 ResultVO 返回
        Long userId = principal.getUserId();
        // 用户类型
        String userType = "2";
        // 生成jwt
        String token = JsonWebTokenUtil.getJwtStr(userId, dto.getDevice(), userType);
        principal.setToken(token);
        // 在线时长
        int endDay = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.APP_LOGIN_TIME));
        String key = RedisKeyConstant.getLoginStatus(userId);
        // 登录状态持久化
        loginStatusService.loginStatusToRedis(key, principal, endDay);
        return token;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String loginByVerificationCode(LoginVerificationCodeDTO dto) {
        String verificationCode = dto.getVerificationCode();
        String phone = dto.getPhone();
        if (!SmsConstant.PASS_CODE.equals(verificationCode)) {
            boolean flag = phoneCodeService.checkVerificationCode(phone, verificationCode);
            if (!flag) {
                throw new ServiceException("验证码错误或已失效！请重新获取");
            }
            // 修改验证码为已使用
            phoneCodeService.updateVerificationCodeStatus(phone, verificationCode);
        }

        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (user == null) {
            user = new User(phone, phone, UserConstant.DEF_PROFILE_PATH, UserConstant.DEF_COVER_PATH,
                UserConstant.DEF_GENDER, new Date(), RegisterTypeConstant.USER_REGISTER);
            user.setPassword(EncryptUtil.encryptSha256(UserConstant.DEF_PASSWORD));
            userService.save(user);
        }
        LoginPasswordDTO loginPasswordDTO =
            new LoginPasswordDTO(UserConstant.DEF_PASSWORD, phone, AuthConstant.DEVICE_ANDROID);
        return this.login(loginPasswordDTO);
    }

    @Override
    public void logout(String token) {
        Long userId = -1L;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(token);
            userId = (Long)claims.get("userId");
        } catch (Exception e) {
            logger.error("token解析失败", e);
        }
        if (userId != -1) {
            String key = RedisKeyConstant.getLoginStatus(userId);
            // 移除登录状态
            loginStatusService.removeInRedis(key, token);
            SecurityContextHolder.clearContext();
        }
    }

}
