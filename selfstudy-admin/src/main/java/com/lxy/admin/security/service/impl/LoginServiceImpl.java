package com.lxy.admin.security.service.impl;

import com.lxy.system.dto.LoginVerifyCodeDTO;
import com.lxy.admin.security.service.LoginService;
import com.lxy.common.domain.R;
import com.lxy.common.properties.CustomProperties;
import com.lxy.common.constant.CommonConstant;
import com.lxy.common.constant.ConfigConstant;

import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.framework.security.domain.StatelessUser;
import com.lxy.framework.security.service.LoginStatusService;
import com.lxy.system.service.BusinessConfigService;
import com.lxy.system.service.RedisService;
import com.lxy.common.util.JsonWebTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2023/02/23 21:08
 * @version 1.0
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
    private RedisService redisService;

    @Override
    public R<Object> login(LoginVerifyCodeDTO dto, HttpServletResponse response) {
        boolean flag = checkVerifyCode(dto.getVerifyCode(), dto.getUuid());
        if (!flag){
            return R.fail("验证码错误或已失效！");
        }
        //AuthenticationManager authenticate进行用户认证
        // 会去调用UserDetailsService.loadUserByUsername方法。AdminDetailsServiceImpl 实现了 UserDetailsService接口的 loadUserByUsername方法
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        }catch (Exception e){
            logger.error("用户名或密码错误",e);
            return R.fail("用户名或密码错误！");
        }
        //如果认证通过了，使用userid生成一个jwt jwt存入 ResultVO 返回
        StatelessUser principal = (StatelessUser)authenticate.getPrincipal();
        Integer adminId = principal.getUserId();
        //用户类型
        String userType = "1";
        //生成jwt
        String token = JsonWebTokenUtil.getJwtStr(adminId, dto.getDevice(), userType);
        principal.setToken(token);
        //在线时长
        int endDay = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.ADMIN_LOGIN_TIME));
        String key = RedisKeyConstant.getAdminLoginStatus(adminId);
        // 登录状态持久化
        loginStatusService.loginStatusToRedis(key,principal, endDay);
        //设置客户端cookie
        Cookie cookie = new Cookie(CommonConstant.TOKEN_NAME_ADMIN, token);
        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setPath(CustomProperties.contentPath);
        response.addCookie(cookie);
        return R.ok(token);
    }

    @Override
    public void logout(String token, HttpServletRequest request,HttpServletResponse response) {
        Integer userId = -1;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(token);
            userId = (Integer) claims.get("userId");
        }catch (Exception e){
            logger.error("token解析失败",e);
        }
        if (userId != -1){
            String key = RedisKeyConstant.getAdminLoginStatus(userId);
            //移除登录状态
            loginStatusService.removeInRedis(key,token);
            SecurityContextHolder.clearContext();
            //删除客户端cookie
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (CommonConstant.TOKEN_NAME_ADMIN.equals(cookie.getName())){
                    Cookie c = new Cookie(CommonConstant.TOKEN_NAME_ADMIN, null);
                    c.setMaxAge(0);
                    c.setPath(CustomProperties.contentPath);
                    response.addCookie(c);
                    break;
                }
            }
        }
    }

    /**
     * 校验验证码
     * @author jiacheng yang.
     * @since 2025/03/07 14:50
     */
    private boolean checkVerifyCode(String verifyCode, String uuid) {
        boolean flag = false;
        String verifyKey = RedisKeyConstant.getMathCodeKey(uuid);
        String code = redisService.getObject(verifyKey,String.class);
        if (code != null && code.equals(verifyCode)){
            flag = true;
        }
        return flag;
    }


}
