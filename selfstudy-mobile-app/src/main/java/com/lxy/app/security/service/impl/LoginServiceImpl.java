package com.lxy.app.security.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.lxy.app.security.service.LoginService;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.bo.R;
import com.lxy.common.security.bo.StatelessAdmin;
import com.lxy.common.security.bo.StatelessUser;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.service.RedisService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.JsonWebTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/22 17:54
 * @Version: 1.0
 */

@Service
public class LoginServiceImpl implements LoginService {

    private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private  AuthenticationManager authenticationManager;
    @Resource
    private  RedisService redisService;
    @Resource
    private  BusinessConfigService businessConfigService;


    @Override
    public R<Object> login(String username, String password, String device) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        }catch (Exception e){
            logger.error("用户名或密码错误",e);
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
        loginStatusToRedisUser(key,principal, endDay);
        return R.ok(token);
    }

    @Override
    public void logout(String token) {
        Integer userId = -1;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(token);
            userId = (Integer) claims.get("userId");
        }catch (Exception e){
            logger.error("token解析失败",e);
        }
        if (userId != -1){
            String key = RedisKeyConstant.getLoginStatus(userId);
            //移除登录状态
            removeInRedisUser(key,token);
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * 登录状态持久化到redis
     * @param statelessUser 用户信息
     * @param endDay 过期时长单位天
     */
    private void loginStatusToRedisUser(String key, StatelessUser statelessUser, int endDay){
        Date now = new Date();
        statelessUser.setLoginTime(now);

        Date end = DateUtil.offsetDay(now, endDay);
        //设置过期时间
        statelessUser.setEndTime(end);

        List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
        if (CollUtil.isEmpty(loginList)){
            loginList = new ArrayList<>(1);
        }
        loginList.add(statelessUser);
        redisService.setObject(key, loginList, -1L, null);
    }

    private void removeInRedisUser(String key, String token) {
        List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
        if (CollUtil.isNotEmpty(loginList)){
            loginList.removeIf(o -> o.getToken().equals(token));
            if (loginList.isEmpty()){
                redisService.deleteKey(key);
            }else {
                redisService.setObject(key, loginList, -1L, null);
            }
        }
    }

}
