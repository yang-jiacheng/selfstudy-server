package com.lxy.app.security.service.impl;

import com.lxy.app.security.service.LoginService;
import com.lxy.common.constant.ConfigConstants;
import com.lxy.common.bo.R;
import com.lxy.common.security.bo.StatelessUser;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.redis.util.RedisKeyUtil;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.util.JsonWebTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/22 17:54
 * @Version: 1.0
 */

@Service
public class LoginServiceImpl implements LoginService {

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
    public R<Object> login(String username, String password, String device) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(-1,"用户名或密码错误！");
        }
        StatelessUser principal = (StatelessUser) authenticate.getPrincipal();
        //如果认证通过了，使用userid生成一个jwt jwt存入 ResultVO 返回
        Integer userId = principal.getUserInfo().getId();
        //用户类型
        String userType = "2";
        //生成jwt
        String token = JsonWebTokenUtil.getJwtStr(userId, device, userType);
        principal.setToken(token);
        //一个号在线数
        int onlineNum = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstants.APP_HAS_NUM));
        //在线时长
        int endDay = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstants.APP_LOGIN_TIME));
        String key = RedisKeyUtil.getLoginStatus(userId);
        // 登录状态持久化
        commonRedisService.loginStatusToRedisUser(key,principal, endDay);
        //控制一个账号在线数
        commonRedisService.controlLoginNumUser(key,onlineNum,endDay,token);
        return R.ok(token);
    }

    @Override
    public void logout(String token) {
        Integer userId = -1;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(token);
            userId = (Integer) claims.get("userId");
        }catch (Exception e){
            e.printStackTrace();
        }
        if (userId != -1){
            String key = RedisKeyUtil.getLoginStatus(userId);
            //移除登录状态
            commonRedisService.removeInRedisUser(key,token);
            SecurityContextHolder.clearContext();
        }
    }
}
