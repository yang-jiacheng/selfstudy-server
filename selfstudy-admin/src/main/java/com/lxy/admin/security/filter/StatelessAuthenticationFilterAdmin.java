package com.lxy.admin.security.filter;

import cn.hutool.core.collection.CollUtil;
import com.lxy.system.service.AdminInfoService;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.system.service.RedisService;
import com.lxy.framework.security.domain.StatelessUser;
import com.lxy.framework.security.service.LoginStatusService;
import com.lxy.framework.security.wrapper.CustomHttpServletRequestWrapper;
import com.lxy.system.service.BusinessConfigService;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.common.util.LogUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.lxy.common.constant.ConfigConstant.*;
import static com.lxy.common.constant.CommonConstant.*;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2023/02/23 21:17
 * @version 1.0
 */

public class StatelessAuthenticationFilterAdmin extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(StatelessAuthenticationFilterAdmin.class);

    private final BusinessConfigService businessConfigService;

    private final LoginStatusService loginStatusService;

    private final AdminInfoService adminInfoService;

    private final RedisService redisService;

    public StatelessAuthenticationFilterAdmin(BusinessConfigService businessConfigService, LoginStatusService loginStatusService,
                                              AdminInfoService adminInfoService, RedisService redisService) {
        this.businessConfigService = businessConfigService;
        this.loginStatusService = loginStatusService;
        this.adminInfoService = adminInfoService;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token

        String accessToken = JsonWebTokenUtil.getAccessToken(request, TOKEN_NAME_ADMIN);

        if (accessToken == null){
            logger.error("token未获取到");
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        Integer userId = null;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(accessToken);
            userId = (Integer) claims.get("userId");
        }catch (Exception e){
            logger.error("token解析失败",e);
            filterChain.doFilter(request, response);
            return;
        }
        //一个号在线数
        int onlineNum = Integer.parseInt(businessConfigService.getBusinessConfigValue(ADMIN_HAS_NUM));
        //在线时长
        int endDay = Integer.parseInt(businessConfigService.getBusinessConfigValue(ADMIN_LOGIN_TIME));
        String key = RedisKeyConstant.getAdminLoginStatus(userId);
        //控制一个账号在线数
        StatelessUser loginStatus = loginStatusService.controlLoginNum(key,  onlineNum, endDay,accessToken);
        if (loginStatus == null){
            logger.error("无法识别的登录状态");
            filterChain.doFilter(request, response);
            return;
        }
        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginStatus,null,loginStatus.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //输出日志
        String msg  = LogUtil.logOperation(userId, request,"");
        logger.warn(msg);
        //放行
        filterChain.doFilter(request, response);

    }

}
