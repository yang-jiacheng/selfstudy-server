package com.lxy.admin.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.framework.security.service.LoginStatusService;
import com.lxy.system.service.RedisService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Set;

/**
 * 双令牌会话清理定时任务
 * @author jiacheng yang.
 * @since 2025/01/18 16:00
 * @version 1.0
 */
@Slf4j
@Component
public class TokenCleanupTask {

    @Resource
    private LoginStatusService loginStatusService;

    @Resource
    private RedisService redisService;

    /**
     * 每天凌晨1点05分执行一次深度清理
     * 清理可能遗留的无效数据
     */
    @XxlJob("deepCleanupAdminLoginStatus")
    public void deepCleanupAdminLoginStatus() {
        log.info("开始深度清理双令牌会话数据");
        TimeInterval timer = DateUtil.timer();
        try {
            Set<String> sessionKeys = redisService.scanKeys("ADMIN_DUAL_TOKEN_SESSIONS:admin_*", null);
            if (sessionKeys.isEmpty()) {
                log.info("没有找到管理员会话数据");
                log.info("深度清理双令牌会话结束，耗时：{}", DateUtil.formatBetween(timer.intervalMs()));
                return;
            }

            for (String key : sessionKeys) {
                loginStatusService.cleanExpiredSessions(key);
            }
        } catch (Exception e) {
            log.error("深度清理双令牌会话时发生异常", e);
        }
        log.info("深度清理双令牌会话结束，耗时：{}", DateUtil.formatBetween(timer.intervalMs()));
    }

}
