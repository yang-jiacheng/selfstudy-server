package com.lxy.app.security.service.impl;

import com.lxy.app.security.service.LoginService;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.domain.R;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.framework.security.domain.StatelessUser;
import com.lxy.framework.security.service.LoginStatusService;
import com.lxy.system.service.BusinessConfigService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * TODO
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


    @Override
    public R<Object> login(String username, String password, String device) {
        //AuthenticationManager authenticate进行用户认证
        // 会去调用UserDetailsService.loadUserByUsername方法。UserDetailsServiceImpl 实现了 UserDetailsService接口的 loadUserByUsername方法
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            logger.error("用户名或密码错误", e);
            return R.fail("用户名或密码错误！");
        }
        StatelessUser principal = (StatelessUser) authenticate.getPrincipal();
        //如果认证通过了，使用userid生成一个jwt jwt存入 ResultVO 返回
        Integer userId = principal.getUserId();
        //用户类型
        String userType = "2";
        //生成jwt
        String token = JsonWebTokenUtil.getJwtStr(userId, device, userType);
        principal.setToken(token);
        //在线时长
        int endDay = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.APP_LOGIN_TIME));
        String key = RedisKeyConstant.getLoginStatus(userId);
        // 登录状态持久化
        loginStatusService.loginStatusToRedis(key, principal, endDay);
        return R.ok(token);
    }

    @Override
    public void logout(String token) {
        Integer userId = -1;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(token);
            userId = (Integer) claims.get("userId");
        } catch (Exception e) {
            logger.error("token解析失败", e);
        }
        if (userId != -1) {
            String key = RedisKeyConstant.getLoginStatus(userId);
            //移除登录状态
            loginStatusService.removeInRedis(key, token);
            SecurityContextHolder.clearContext();
        }
    }


}
