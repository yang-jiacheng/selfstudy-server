package com.lxy.framework.security.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.lxy.common.domain.TokenPair;
import com.lxy.framework.security.domain.StatelessUser;
import com.lxy.system.service.RedisService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 登录状态服务
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Slf4j
@Service
public class LoginStatusService {

    @Resource
    private RedisService redisService;

    /**
     * 传统单令牌登录状态管理
     */
    public void removeInRedis(String key, String token) {
        List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
        if (CollUtil.isNotEmpty(loginList)) {
            loginList.removeIf(o -> o.getToken().equals(token));
            if (loginList.isEmpty()) {
                redisService.deleteKey(key);
            } else {
                redisService.setObject(key, loginList, -1L, null);
            }
        }
    }

    /**
     * 登录状态持久化到redis
     *
     * @param statelessUser 用户信息
     * @param endDay        过期时长单位天
     */
    public void loginStatusToRedis(String key, StatelessUser statelessUser, int endDay) {
        Date now = new Date();
        statelessUser.setLoginTime(now);

        Date end = DateUtil.offsetDay(now, endDay);
        //设置过期时间
        statelessUser.setEndTime(end);
        List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
        if (CollUtil.isEmpty(loginList)) {
            loginList = new ArrayList<>(1);
        }
        loginList.add(statelessUser);
        redisService.setObject(key, loginList, -1L, null);
    }

    /**
     * 控制一个账号在线数
     *
     * @param key       缓存key
     * @param onlineNum 一个账号在线数
     * @param endDay    在线时长 天
     * @param token     token
     * @author jiacheng yang.
     * @since 2025/03/06 18:55
     */
    public StatelessUser controlLoginNum(String key, Integer onlineNum, int endDay, String token) {
        Date now = new Date();

        StatelessUser loginStatus = null;
        if (redisService.hasKey(key)) {
            List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
            if (CollUtil.isNotEmpty(loginList)) {
                //删除过期的jwt
                loginList.removeIf(o -> DateUtil.compare(now, o.getEndTime()) > 0);
                //按过期时间正序
                loginList.sort(Comparator.comparing(StatelessUser::getEndTime));
                int size = loginList.size();
                if (size > onlineNum) {
                    loginList.remove(0);
                }
                for (StatelessUser status : loginList) {
                    String accessToken = status.getToken();
                    if (token.equals(accessToken)) {
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
     * 获取登录状态
     *
     * @author jiacheng yang.
     * @since 2025/4/2 16:36
     */
    public StatelessUser getLoginStatus(String key, String token) {
        StatelessUser loginStatus = null;
        if (redisService.hasKey(key)) {
            List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
            if (CollUtil.isEmpty(loginList)) {
                return null;
            }

            loginStatus = loginList.stream()
                    .filter(status -> token.equals(status.getToken()))
                    .findFirst()
                    .orElse(null);
        }
        return loginStatus;
    }

    // ========== 双令牌会话管理 ==========

    /**
     * 管理用户会话
     *
     * @param userId      用户ID
     * @param sessionKey  Redis会话键
     * @param tokenPair   令牌对
     * @param maxSessions 最大会话数
     */
    public void manageUserSessions(Integer userId, String sessionKey, TokenPair tokenPair, int maxSessions) {
        // 清理过期会话
        cleanExpiredSessions(sessionKey);

        // 检查会话数量是否超过限制
        Long sessionCount = redisService.getSortedSetSize(sessionKey);
        if (sessionCount != null && sessionCount >= maxSessions) {
            // 删除最早过期的会话
            long deleteCount = sessionCount - maxSessions + 1;
            redisService.removeFromSortedSetByRange(sessionKey, 0, deleteCount - 1);
        }

        // 添加新会话，使用RefreshToken过期时间作为分数
        double score = tokenPair.getRefreshExpires().getTime();
        redisService.addToSortedSet(sessionKey, tokenPair, score);
        Long curSessionCount = redisService.getSortedSetSize(sessionKey);
        log.info("用户`{}`会话管理完成，当前会话数: {}", userId, curSessionCount);
    }

    /**
     * 清理过期会话
     *
     * @param sessionKey Redis键
     */
    public void cleanExpiredSessions(String sessionKey) {
        long currentTime = System.currentTimeMillis();
        redisService.removeFromSortedSetByScore(sessionKey, 0, currentTime);
    }


    /**
     * 通过RefreshId移除会话
     *
     * @param sessionKey Redis键
     * @param refreshId  刷新令牌ID
     */
    public void removeSessionByRefreshId(String sessionKey, String refreshId) {
        Set<TokenPair> sessions = redisService.getSortedSetRange(sessionKey, 0, -1);
        if (sessions != null) {
            sessions.stream()
                    .filter(session -> refreshId.equals(session.getRefreshId()))
                    .findFirst()
                    .ifPresent(session -> {
                        redisService.removeFromSortedSet(sessionKey, session);
                        log.info("已移除会话，sessionKey: {}, refreshId: {}", sessionKey, refreshId);
                    });
        }
    }

    /**
     * 检查会话是否存在
     *
     * @param sessionKey Redis键
     * @param refreshId  刷新令牌ID
     * @return 是否存在
     */
    public boolean isSessionExists(String sessionKey, String refreshId) {
        Set<TokenPair> sessions = redisService.getSortedSetRange(sessionKey, 0, -1);
        if (sessions != null) {
            return sessions.stream()
                    .anyMatch(session -> refreshId.equals(session.getRefreshId()));
        }
        return false;
    }


    /**
     * 获取用户会话数量
     *
     * @param sessionKey 会话键
     * @return 会话数量
     */
    public long getSessionCount(String sessionKey) {

        // 清理过期会话
        cleanExpiredSessions(sessionKey);

        // 获取会话数量
        Long count = redisService.getSortedSetSize(sessionKey);
        return count != null ? count : 0;

    }

    /**
     * 清除用户所有会话
     *
     * @param sessionKey 会话键
     */
    public void clearAllSessions(String sessionKey) {
        redisService.deleteKey(sessionKey);
        log.info("已清除所有会话: {}", sessionKey);

    }

}
