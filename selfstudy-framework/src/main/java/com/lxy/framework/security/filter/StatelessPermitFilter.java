package com.lxy.framework.security.filter;

import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.enums.LogUserType;
import com.lxy.common.util.DualTokenUtil;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.framework.security.service.LoginStatusService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.lxy.common.util.DualTokenUtil.TOKEN_NAME_ADMIN;
import static com.lxy.common.util.DualTokenUtil.TOKEN_NAME_APP;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2024/08/30 16:52
 */
public class StatelessPermitFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(StatelessPermitFilter.class);

    private final Integer loginUserType;

    private final LoginStatusService loginStatusService;

    public StatelessPermitFilter(Integer loginUserType, LoginStatusService loginStatusService) {
        this.loginUserType = loginUserType;
        this.loginStatusService = loginStatusService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String tokenKey = loginUserType.equals(LogUserType.ADMIN.type) ? TOKEN_NAME_ADMIN : TOKEN_NAME_APP;

        // 访问的地址
        String accessToken = DualTokenUtil.getToken(request, tokenKey);
        if (accessToken == null) {
            // 放行
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        Long userId = null;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(accessToken);
            userId = (Integer)claims.get("userId");
        } catch (Exception e) {
            LOG.error("解析token失败", e);
            // 放行
            filterChain.doFilter(request, response);
            return;
        }
        String loginStatusKey = loginUserType.equals(LogUserType.ADMIN.type) ? RedisKeyConstant.getAdminInfo(userId)
            : RedisKeyConstant.getLoginStatus(userId);

        // //存入SecurityContextHolder
        // UsernamePasswordAuthenticationToken authenticationToken =
        // new UsernamePasswordAuthenticationToken(loginStatus,null,loginStatus.getAuthorities());
        // SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // //输出日志
        // String msg = LogUtil.logOperation(userId, request,"");
        // logger.warn(msg);
        // 放行
        filterChain.doFilter(request, response);

    }
}
