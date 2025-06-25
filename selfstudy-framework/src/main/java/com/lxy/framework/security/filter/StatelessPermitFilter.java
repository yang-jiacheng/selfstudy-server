package com.lxy.framework.security.filter;

import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.enums.LogUserType;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.common.util.LogUtil;
import com.lxy.framework.security.domain.StatelessUser;
import com.lxy.framework.security.service.LoginStatusService;
import com.lxy.framework.security.wrapper.CustomHttpServletRequestWrapper;
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

import static com.lxy.common.constant.CommonConstant.*;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2024/08/30 16:52
 * @version 1.0
 */
public class StatelessPermitFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(StatelessPermitFilter.class);

    private final Integer loginUserType ;

    private final LoginStatusService loginStatusService;

    public StatelessPermitFilter(Integer loginUserType, LoginStatusService loginStatusService) {
        this.loginUserType = loginUserType;
        this.loginStatusService = loginStatusService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenKey = loginUserType.equals(LogUserType.ADMIN.type) ? TOKEN_NAME_ADMIN : TOKEN_NAME_APP;

        // 访问的地址
        String accessToken = JsonWebTokenUtil.getAccessToken(request, tokenKey);
        if (accessToken == null){
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        Integer userId = null;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(accessToken);
            userId = (Integer) claims.get("userId");
        }catch (Exception e) {
            LOG.error("解析token失败", e);
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        String loginStatusKey = loginUserType.equals(LogUserType.ADMIN.type) ? RedisKeyConstant.getAdminLoginStatus(userId) : RedisKeyConstant.getLoginStatus(userId);
        //控制一个账号在线数
        StatelessUser loginStatus = loginStatusService.getLoginStatus(loginStatusKey,accessToken);
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
        // 创建 CustomHttpServletRequestWrapper，包装原始的 HttpServletRequest
        CustomHttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequestWrapper(request);
        String requestBody = wrappedRequest.getBody();
        String msg  = LogUtil.logOperation(userId, request,requestBody);
        logger.warn(msg);
        //放行
        filterChain.doFilter(wrappedRequest, response);

    }
}
