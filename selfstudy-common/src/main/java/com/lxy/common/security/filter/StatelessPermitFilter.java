package com.lxy.common.security.filter;

import com.lxy.common.constant.CommonConstants;
import com.lxy.common.constant.ConfigConstants;
import com.lxy.common.domain.StatelessUser;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.redis.util.RedisKeyUtil;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.util.IpUtil;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.common.util.LogUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2024/08/30 16:52
 * @Version: 1.0
 */
public class StatelessPermitFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(StatelessPermitFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 访问的地址
        String accessToken = JsonWebTokenUtil.getAccessToken(request, CommonConstants.COOKIE_NAME_APP);
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
