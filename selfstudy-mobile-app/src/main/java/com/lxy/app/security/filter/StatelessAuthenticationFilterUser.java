package com.lxy.app.security.filter;

import com.lxy.common.constant.CommonConstants;
import com.lxy.common.constant.ConfigConstants;
import com.lxy.common.security.bo.StatelessUser;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.redis.util.RedisKeyUtil;
import com.lxy.common.security.CustomHttpServletRequestWrapper;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.common.util.LogUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/22 17:56
 * @Version: 1.0
 */
public class StatelessAuthenticationFilterUser extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(StatelessAuthenticationFilterUser.class);

    private final BusinessConfigService businessConfigService;

    private final CommonRedisService commonRedisService;

    public StatelessAuthenticationFilterUser(BusinessConfigService businessConfigService, CommonRedisService commonRedisService) {
        this.businessConfigService = businessConfigService;
        this.commonRedisService = commonRedisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String msg = "";
        String accessToken = JsonWebTokenUtil.getAccessToken(request, CommonConstants.COOKIE_NAME_APP);
        if (accessToken == null){
            msg = "token未获取到";
            logger.warn(msg);
            SecurityContextHolder.clearContext();
            throw new InsufficientAuthenticationException(msg);

        }

        //解析token
        Integer userId = null;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(accessToken);
            userId = (Integer) claims.get("userId");
        }catch (Exception e){
            msg = "token非法";
            logger.warn(msg);
            throw new InsufficientAuthenticationException(msg);
        }
        //一个号在线数
        int onlineNum = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstants.APP_HAS_NUM));
        //在线时长
        int endDay = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstants.APP_LOGIN_TIME));
        String key = RedisKeyUtil.getLoginStatus(userId);
        //控制一个账号在线数
        StatelessUser loginStatus = commonRedisService.controlLoginNumUser(key,  onlineNum, endDay,accessToken);
        if (loginStatus == null){
            msg = "无法识别的登录状态";
            logger.warn(msg);
            throw new BadCredentialsException(msg);
        }
        //存入SecurityContextHolder
        //获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginStatus,null,loginStatus.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 创建 CustomHttpServletRequestWrapper，包装原始的 HttpServletRequest
        CustomHttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequestWrapper(request);
        String requestBody = wrappedRequest.getBody();
        msg = LogUtil.logOperation(userId, request,requestBody);
        logger.warn(msg);
        //放行
        filterChain.doFilter(request, response);

    }
}
