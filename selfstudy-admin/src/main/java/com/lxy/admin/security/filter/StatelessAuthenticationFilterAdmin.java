package com.lxy.admin.security.filter;

import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.security.bo.StatelessUser;
import com.lxy.common.security.serviice.LoginStatusService;
import com.lxy.common.security.wrapper.CustomHttpServletRequestWrapper;
import com.lxy.common.service.AdminInfoService;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.service.RedisService;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.common.util.LogUtil;
import com.lxy.common.util.WebUtil;
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
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.lxy.common.constant.ConfigConstant.*;
import static com.lxy.common.constant.CommonConstant.*;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/23 21:17
 * @Version: 1.0
 */

public class StatelessAuthenticationFilterAdmin extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(StatelessAuthenticationFilterAdmin.class);

    private final BusinessConfigService businessConfigService;

    private final LoginStatusService loginStatusService;

    public StatelessAuthenticationFilterAdmin(BusinessConfigService businessConfigService, LoginStatusService loginStatusService) {
        this.businessConfigService = businessConfigService;
        this.loginStatusService = loginStatusService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String msg = "";
        String accessToken = JsonWebTokenUtil.getAccessToken(request, COOKIE_NAME_ADMIN);

        if (accessToken == null){
            logger.error("token未获取到");
            needLogin(response);
            return;
        }
        //解析token
        Integer userId = null;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(accessToken);
            userId = (Integer) claims.get("userId");
        }catch (Exception e){
            logger.error("token解析失败",e);
            needLogin(response);
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
            needLogin(response);
            return;
        }

        //更新权限
        loginStatusService.updatePermissions(key,userId,loginStatus);

        //存入SecurityContextHolder
        //获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginStatus,null,loginStatus.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //输出日志
        // 创建 CustomHttpServletRequestWrapper，包装原始的 HttpServletRequest
        CustomHttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequestWrapper(request);
        String requestBody = wrappedRequest.getBody();
        msg = LogUtil.logOperation(userId, request,requestBody);
        logger.warn(msg);
        //放行

        filterChain.doFilter(wrappedRequest, response);

    }

    private void needLogin(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        WebUtil.renderRedirect(response,"/login");
    }


}
