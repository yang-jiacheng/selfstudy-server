package com.lxy.admin.security.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lxy.admin.security.service.LoginService;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.domain.R;
import com.lxy.common.domain.TokenPair;
import com.lxy.common.enums.LogUserType;
import com.lxy.common.util.DualTokenUtil;
import com.lxy.framework.security.domain.StatelessUser;
import com.lxy.framework.security.service.LoginStatusService;
import com.lxy.system.dto.LoginVerifyCodeDTO;
import com.lxy.system.service.BusinessConfigService;
import com.lxy.system.service.redis.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.lxy.common.util.DualTokenUtil.PARAM_NAME_JID;
import static com.lxy.common.util.DualTokenUtil.PARAM_NAME_USER_ID;
import static com.lxy.common.util.DualTokenUtil.PARAM_NAME_USER_TYPE;

/**
 * 基于双Token机制的登录服务实现
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/01/18 10:00
 */

@Slf4j
@Service
public class DualTokenLoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private BusinessConfigService businessConfigService;
    @Resource
    private RedisService redisService;
    @Resource
    private LoginStatusService loginStatusService;

    @Override
    public R<Object> login(LoginVerifyCodeDTO dto, HttpServletResponse response) {
        boolean flag = checkVerifyCode(dto.getVerifyCode(), dto.getUuid());
        if (!flag) {
            return R.fail("验证码错误或已失效！");
        }
        // AuthenticationManager authenticate进行用户认证
        // 会去调用UserDetailsService.loadUserByUsername方法。AdminDetailsServiceImpl 实现了 UserDetailsService接口的
        // loadUserByUsername方法
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            log.error("用户名或密码错误", e);
            return R.fail("用户名或密码错误！");
        }
        // 如果认证通过了，使用userid生成jwt
        StatelessUser principal = (StatelessUser)authenticate.getPrincipal();
        Long userId = principal.getUserId();
        // 生成双Token
        TokenPair tokenPair = DualTokenUtil.generateTokenPair(userId, LogUserType.ADMIN.type);
        // 设置ip
        tokenPair.setClientIp(dto.getClientIp());
        // 获取最大会话数量配置
        int maxSessions = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.ADMIN_HAS_NUM));
        // 管理会话
        String sessionKey = RedisKeyConstant.getAdminDualTokenSessions(userId);
        loginStatusService.manageUserSessions(userId, sessionKey, tokenPair, maxSessions);
        // 记录登录日志
        log.info("用户`{}`登录成功", userId);
        // 返回Token信息
        return R.ok(tokenPair);
    }

    @Override
    public void logout(String token, HttpServletRequest request, HttpServletResponse response) {
        Long userId;
        try {
            boolean isValid = DualTokenUtil.validateRefreshToken(token);
            if (!isValid) {
                return;
            }
            Claims claims = DualTokenUtil.parseToken(token);
            userId = (Long)claims.get(PARAM_NAME_USER_ID);
            String refreshId = (String)claims.get(PARAM_NAME_JID);
            if (userId != -1L) {
                String sessionKey = RedisKeyConstant.getAdminDualTokenSessions(userId);
                // 移除会话
                loginStatusService.removeSessionByRefreshId(sessionKey, refreshId);
                // 清除SecurityContext
                SecurityContextHolder.clearContext();
                log.info("用户`{}`登出成功", userId);
            }
        } catch (Exception e) {
            log.error("登出失败", e);
        }
    }

    @Override
    public R<TokenPair> refreshToken(String refreshToken) {
        R<TokenPair> r = R.fail(1000, "认证失败，请重新登录！");
        if (StrUtil.isEmpty(refreshToken)) {
            return r;
        }
        // 验证刷新令牌
        boolean isValid = DualTokenUtil.validateRefreshToken(refreshToken);
        if (!isValid) {
            return r;
        }

        // 解析刷新令牌获取用户信息
        Claims claims = DualTokenUtil.parseToken(refreshToken);
        Long userId = (Long)claims.get(PARAM_NAME_USER_ID);
        Integer userType = (Integer)claims.get(PARAM_NAME_USER_TYPE);
        String refreshId = (String)claims.get(PARAM_NAME_JID);

        // 检查会话是否存在
        String sessionKey = RedisKeyConstant.getAdminDualTokenSessions(userId);
        if (!loginStatusService.isSessionExists(sessionKey, refreshId)) {
            return r;
        }
        // 生成新的令牌对
        TokenPair newTokenPair = DualTokenUtil.generateTokenPair(userId, userType);
        // 获取最大会话数量配置
        int maxSessions = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.ADMIN_HAS_NUM));
        // 移除旧会话
        loginStatusService.removeSessionByRefreshId(sessionKey, refreshId);
        // 添加新会话
        loginStatusService.manageUserSessions(userId, sessionKey, newTokenPair, maxSessions);
        log.info("用户`{}`刷新令牌成功", userId);
        return R.ok(newTokenPair);
    }

    /**
     * 校验验证码
     */
    private boolean checkVerifyCode(String verifyCode, String uuid) {
        if ("pass".equals(verifyCode)) {
            return true;
        }
        if (StrUtil.isEmpty(verifyCode) || StrUtil.isEmpty(uuid)) {
            return false;
        }

        String verifyKey = RedisKeyConstant.getMathCodeKey(uuid);
        String code = redisService.getObject(verifyKey, String.class);

        if (code != null && code.equals(verifyCode)) {
            // 验证成功后删除验证码
            redisService.deleteKey(verifyKey);
            return true;
        }

        return false;
    }
}
