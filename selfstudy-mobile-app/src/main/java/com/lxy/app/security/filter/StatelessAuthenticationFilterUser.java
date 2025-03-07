package com.lxy.app.security.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.lxy.common.bo.R;
import com.lxy.common.constant.CommonConstant;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.security.bo.StatelessUser;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.security.CustomHttpServletRequestWrapper;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.service.RedisService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.JsonWebTokenUtil;
import com.lxy.common.util.LogUtil;
import com.lxy.common.util.WebUtil;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/22 17:56
 * @Version: 1.0
 */
public class StatelessAuthenticationFilterUser extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(StatelessAuthenticationFilterUser.class);

    private final BusinessConfigService businessConfigService;

    private final RedisService redisService;

    public StatelessAuthenticationFilterUser(BusinessConfigService businessConfigService, RedisService redisService) {
        this.businessConfigService = businessConfigService;
        this.redisService = redisService;
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
        StatelessUser loginStatus  = controlLoginNumUser(key,  onlineNum, endDay,accessToken);
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
        logger.warn(msg);
        //放行
        filterChain.doFilter(request, response);

    }

    private void needLogin(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        WebUtil.renderString(response, MSG);
    }

    /**
     * 控制一个账号在线数
     * @author jiacheng yang.
     * @since 2025/03/06 18:55
     * @param key 缓存key
     * @param onlineNum 一个账号在线数
     * @param endDay 在线时长 天
     * @param token token
     */
    public StatelessUser controlLoginNumUser(String key, Integer onlineNum, int endDay, String token){
        Date now = new Date();

        StatelessUser loginStatus = null;
        if (redisService.hasKey(key)){
            List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
            if (CollUtil.isNotEmpty(loginList)){
                //删除过期的jwt
                loginList.removeIf(o -> DateUtil.compare(now, o.getEndTime()) > 0);
                //按过期时间正序
                loginList.sort(Comparator.comparing(StatelessUser::getEndTime));
                int size = loginList.size();
                if (size > onlineNum){
                    loginList.remove(0);
                }
                for (StatelessUser status : loginList) {
                    String accessToken = status.getToken();
                    if (token.equals(accessToken)){
                        Date end = DateUtil.offsetDay(now, endDay);
                        status.setEndTime(end);
                        loginStatus = status;
                        break;
                    }
                }
                redisService.setObject(key, loginList, -1L, null);
            }
        }
        return loginStatus;
    }

}
