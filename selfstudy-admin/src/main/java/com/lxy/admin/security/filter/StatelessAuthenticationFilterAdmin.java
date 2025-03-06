package com.lxy.admin.security.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.lxy.common.constant.CommonConstants;
import com.lxy.common.constant.ConfigConstants;
import com.lxy.common.security.OnlineStatusService;
import com.lxy.common.security.bo.StatelessAdmin;
import com.lxy.common.redis.util.RedisKeyUtil;
import com.lxy.common.security.CustomHttpServletRequestWrapper;
import com.lxy.common.service.AdminInfoService;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/23 21:17
 * @Version: 1.0
 */

public class StatelessAuthenticationFilterAdmin extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(StatelessAuthenticationFilterAdmin.class);

    private final BusinessConfigService businessConfigService;

    private final RedisService redisService;

    private final AdminInfoService adminInfoService;

    public StatelessAuthenticationFilterAdmin(BusinessConfigService businessConfigService, RedisService redisService,
                                              AdminInfoService adminInfoService) {
        this.businessConfigService = businessConfigService;
        this.redisService = redisService;
        this.adminInfoService = adminInfoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String msg = "";
        String accessToken = JsonWebTokenUtil.getAccessToken(request, CommonConstants.COOKIE_NAME_ADMIN);

        if (accessToken == null){
            logger.error("token未获取到");
            WebUtil.renderRedirect(response,"/login");
            return;
        }
        //解析token
        Integer userId = null;
        try {
            Claims claims = JsonWebTokenUtil.getClaimsSign(accessToken);
            userId = (Integer) claims.get("userId");
        }catch (Exception e){
            logger.error("token解析失败",e);
            WebUtil.renderRedirect(response,"/login");
            return;
        }
        //一个号在线数
        int onlineNum = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstants.ADMIN_HAS_NUM));
        //在线时长
        int endDay = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstants.ADMIN_LOGIN_TIME));
        String key = RedisKeyUtil.getAdminLoginStatus(userId);
        //控制一个账号在线数
        StatelessAdmin loginStatus = controlLoginNum(key,  onlineNum, endDay,accessToken);
        if (loginStatus == null){
            logger.error("无法识别的登录状态");
            WebUtil.renderRedirect(response,"/login");
            return;
        }

        //更新权限
        updatePermissions(key,userId,loginStatus);

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

    /**
     * 控制一个账号在线数
     * @author jiacheng yang.
     * @since 2025/03/06 18:55
     * @param key 缓存key
     * @param onlineNum 一个账号在线数
     * @param endDay 在线时长 天
     * @param token token
     */
    public StatelessAdmin controlLoginNum(String key, Integer onlineNum, int endDay, String token){
        Date now = new Date();

        StatelessAdmin loginStatus = null;
        if (redisService.hasKey(key)){
            List<StatelessAdmin> loginList = redisService.getObject(key, ArrayList.class);
            if (CollUtil.isNotEmpty(loginList)){
                //删除过期的jwt
                loginList.removeIf(o -> DateUtil.compare(now, o.getEndTime()) > 0);
                //按过期时间正序
                loginList.sort(Comparator.comparing(StatelessAdmin::getEndTime));
                int size = loginList.size();
                if (size > onlineNum){
                    loginList.remove(0);
                }
                for (StatelessAdmin status : loginList) {
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

    /**
     * 更新权限信息
     * 从缓存中获取权限，若缓存中没有则才正常从数据库中获取
     * 注意：如果用户权限发生改变时，需要将缓存中的数据删除
     * @author jiacheng yang.
     * @since 2025/03/06 18:57
     */
    public void updatePermissions(String key,Integer userId, StatelessAdmin loginStatus){
        if (CollUtil.isEmpty(loginStatus.getPermissions())){
            //根据用户查询权限信息 添加到StatelessAdmin中
            List<String> permissions = adminInfoService.getPermissionsById(userId);
            loginStatus.setPermissions(permissions);
            //修改缓存里的权限
            List<StatelessAdmin> loginList = redisService.getObject(key, ArrayList.class);
            for (StatelessAdmin statelessAdmin : loginList) {
                statelessAdmin.setPermissions(permissions);
            }
            redisService.setObject(key, loginList, -1L, null);
        }
    }


}
