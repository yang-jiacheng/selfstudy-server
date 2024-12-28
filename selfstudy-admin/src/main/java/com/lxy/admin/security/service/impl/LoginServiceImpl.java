package com.lxy.admin.security.service.impl;

import com.lxy.admin.security.service.LoginService;
import com.lxy.common.config.properties.CustomProperties;
import com.lxy.common.constant.CommonConstants;
import com.lxy.common.constant.ConfigConstants;
import com.lxy.common.domain.StatelessAdmin;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.redis.util.RedisKeyUtil;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.common.vo.ResultVO;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/23 21:08
 * @Version: 1.0
 */

@Service
public class LoginServiceImpl implements LoginService {

    private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private final AuthenticationManager authenticationManager;

    private final CommonRedisService commonRedisService;

    private final BusinessConfigService businessConfigService;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager, CommonRedisService commonRedisService, BusinessConfigService businessConfigService) {
        this.authenticationManager = authenticationManager;
        this.commonRedisService = commonRedisService;
        this.businessConfigService = businessConfigService;
    }

    @Override
    public ResultVO login(String username, String password, String device, HttpServletResponse response) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        }catch (Exception e){
            logger.error("用户名或密码错误",e);
            return new ResultVO(-1,"用户名或密码错误！");
        }

        //如果认证没通过，给出对应的提示

        //如果认证通过了，使用userid生成一个jwt jwt存入 ResultVO 返回
        StatelessAdmin principal = (StatelessAdmin)authenticate.getPrincipal();
        Integer adminId = principal.getAdminInfo().getId();
        //用户类型
        String userType = "1";
        //生成jwt
        String token = JsonWebTokenUtil.getJwtStr(adminId, device, userType);
        principal.setToken(token);
        //一个号在线数
        int onlineNum = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstants.ADMIN_HAS_NUM));
        //在线时长
        int endDay = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstants.ADMIN_LOGIN_TIME));
        String key = RedisKeyUtil.getAdminLoginStatus(adminId);
        // 登录状态持久化
        commonRedisService.loginStatusToRedis(key,principal, endDay);
        //控制一个账号在线数
        commonRedisService.controlLoginNum(key,onlineNum,endDay,token);
        //设置客户端cookie
        Cookie cookie = new Cookie(CommonConstants.COOKIE_NAME_ADMIN, token);
        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setPath(CustomProperties.contentPath);
        response.addCookie(cookie);
        return new ResultVO(token);
    }

    @Override
    public void logout(String token, HttpServletRequest request,HttpServletResponse response) {
        Integer userId = -1;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(token);
            userId = (Integer) claims.get("userId");
        }catch (Exception e){
            e.printStackTrace();
        }
        if (userId != -1){
            String key = RedisKeyUtil.getAdminLoginStatus(userId);
            //移除登录状态
            commonRedisService.removeInRedis(key,token);
            SecurityContextHolder.clearContext();
            //删除客户端cookie
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (CommonConstants.COOKIE_NAME_ADMIN.equals(cookie.getName())){
                    Cookie c = new Cookie(CommonConstants.COOKIE_NAME_ADMIN, null);
                    c.setMaxAge(0);
                    c.setPath(CustomProperties.contentPath);
                    response.addCookie(c);
                    break;
                }
            }
        }
    }
}
