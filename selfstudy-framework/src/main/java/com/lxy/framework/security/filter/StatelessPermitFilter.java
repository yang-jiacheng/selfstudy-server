package com.lxy.framework.security.filter;

import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.common.util.LogUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2024/08/30 16:52
 * @Version: 1.0
 */
public class StatelessPermitFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(StatelessPermitFilter.class);

    private final String LOGIN_STATUS_KEY ;

    public StatelessPermitFilter(String LOGIN_STATUS_KEY) {
        this.LOGIN_STATUS_KEY = LOGIN_STATUS_KEY;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 访问的地址
        String accessToken = JsonWebTokenUtil.getAccessToken(request, LOGIN_STATUS_KEY);
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
        }catch (Exception e){
            LOG.error("解析token失败",e);
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        String msg = LogUtil.logOperation(userId, request,null);
        LOG.warn(msg);
        //放行
        filterChain.doFilter(request, response);

    }
}
