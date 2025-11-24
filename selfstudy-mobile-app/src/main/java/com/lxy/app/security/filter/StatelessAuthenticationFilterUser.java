package com.lxy.app.security.filter;

import com.lxy.common.constant.AuthConstant;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.common.util.LogUtil;
import com.lxy.framework.security.domain.StatelessUser;
import com.lxy.framework.security.service.LoginStatusService;
import com.lxy.system.service.BusinessConfigService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2023/02/22 17:56
 */
public class StatelessAuthenticationFilterUser extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(StatelessAuthenticationFilterUser.class);

    private final BusinessConfigService businessConfigService;

    private final LoginStatusService loginStatusService;

    public StatelessAuthenticationFilterUser(BusinessConfigService businessConfigService,
        LoginStatusService loginStatusService) {
        this.businessConfigService = businessConfigService;
        this.loginStatusService = loginStatusService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        // 获取token
        String msg = "";
        String accessToken = JsonWebTokenUtil.getAccessToken(request, AuthConstant.TOKEN_NAME_APP);
        if (accessToken == null) {
            logger.error("token未获取到");
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        Long userId = null;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(accessToken);
            userId = (Integer)claims.get("userId");
        } catch (Exception e) {
            logger.error("token解析失败", e);
            filterChain.doFilter(request, response);
            return;
        }
        // 一个号在线数
        int onlineNum = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.APP_HAS_NUM));
        // 在线时长
        int endDay = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.APP_LOGIN_TIME));
        String key = RedisKeyConstant.getLoginStatus(userId);
        // 控制一个账号在线数
        StatelessUser loginStatus = loginStatusService.controlLoginNum(key, onlineNum, endDay, accessToken);
        if (loginStatus == null) {
            logger.error("无法识别的登录状态");
            filterChain.doFilter(request, response);
            return;
        }
        // 存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginStatus, null, loginStatus.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        msg = LogUtil.logOperation(userId, request, "");
        logger.info(msg);
        // 放行
        filterChain.doFilter(request, response);
    }

}
