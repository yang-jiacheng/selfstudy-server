package com.lxy.admin.security.filter;

import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.domain.R;
import com.lxy.common.domain.TokenPair;
import com.lxy.common.util.DualTokenUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.LogUtil;
import com.lxy.common.util.WebUtil;
import com.lxy.framework.security.domain.StatelessUser;
import com.lxy.framework.security.service.LoginStatusService;
import com.lxy.system.service.AdminInfoService;
import com.lxy.system.service.RedisService;
import com.lxy.system.vo.AdminInfoVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

import static com.lxy.common.util.DualTokenUtil.*;

/**
 * 双令牌认证过滤器
 * 基于Access Token + Refresh Token的无状态认证
 * @author jiacheng yang.
 * @since 2025/01/18 15:30
 * @version 1.0
 */
@Slf4j
public class DualTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AdminInfoService adminInfoService;

    public DualTokenAuthenticationFilter(AdminInfoService adminInfoService) {
        this.adminInfoService = adminInfoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取Access Token
        String accessToken = DualTokenUtil.getToken(request, TOKEN_NAME_ADMIN);

        if (accessToken == null) {
            //未获取到就放行，交给Spring Security处理，最终会进入 AuthenticationEntryPointAdminImpl 处理
            logger.error("token未获取到");
            filterChain.doFilter(request, response);
            return;
        }

        // 验证Access Token
        if (!DualTokenUtil.validateAccessToken(accessToken)) {
            log.error("token无效或已过期");
            String result = JsonUtil.toJson(R.fail(401, "token无效或已过期!"));
            WebUtil.renderString(response,result);
            return;
        }
        // 解析Access Token
        Claims claims = DualTokenUtil.parseToken(accessToken);
        Integer userId = (Integer) claims.get(PARAM_NAME_USER_ID);

        // 创建认证用户对象
        StatelessUser statelessUser = new StatelessUser(userId, null, null);
        //设置权限
        AdminInfoVO adminInfo = adminInfoService.getAdminInfoById(userId);
        if (adminInfo == null){
            // 用户不存在，也放行，交给Spring Security处理
            log.error("用户不存在,请重新登录,userId:{}",userId);
            filterChain.doFilter(request, response);
            return;
        }
        statelessUser.setPermissions(adminInfo.getPermissions());
        // 存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(statelessUser, null, statelessUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 输出日志
        String msg = LogUtil.logOperation(userId, request, "");
        log.warn(msg);
        // 放行
        filterChain.doFilter(request, response);
    }

}
