package com.lxy.app.security.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.lxy.common.bo.R;
import com.lxy.common.constant.CommonConstant;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.security.bo.StatelessUser;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.security.serviice.LoginStatusService;
import com.lxy.common.security.wrapper.CustomHttpServletRequestWrapper;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.service.RedisService;
import com.lxy.common.util.JsonUtil;
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

/**
 * TODO
 * @author jiacheng yang.
 * @since  2023/02/22 17:56
 * @version  1.0
 */
public class StatelessAuthenticationFilterUser extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(StatelessAuthenticationFilterUser.class);

    private final BusinessConfigService businessConfigService;

    private final LoginStatusService loginStatusService;

    public StatelessAuthenticationFilterUser(BusinessConfigService businessConfigService,LoginStatusService loginStatusService) {
        this.businessConfigService = businessConfigService;
        this.loginStatusService = loginStatusService;
    }

    private static final String MSG = JsonUtil.toJson(R.fail(1000,"认证失败，请重新登录！"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String msg = "";
        String accessToken = JsonWebTokenUtil.getAccessToken(request, CommonConstant.COOKIE_NAME_APP);
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
        int onlineNum = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.APP_HAS_NUM));
        //在线时长
        int endDay = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.APP_LOGIN_TIME));
        String key = RedisKeyConstant.getLoginStatus(userId);
        //控制一个账号在线数
        StatelessUser loginStatus  = loginStatusService.controlLoginNum(key,  onlineNum, endDay,accessToken);
        if (loginStatus == null){
            logger.error("无法识别的登录状态");
            needLogin(response);
            return;
        }
        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginStatus,null,loginStatus.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 创建 CustomHttpServletRequestWrapper，包装原始的 HttpServletRequest
        CustomHttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequestWrapper(request);
        String requestBody = wrappedRequest.getBody();
        msg = LogUtil.logOperation(userId, request,requestBody);
        logger.info(msg);
        //放行
        filterChain.doFilter(request, response);

    }

    private void needLogin(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        WebUtil.renderString(response, MSG);
    }

}
