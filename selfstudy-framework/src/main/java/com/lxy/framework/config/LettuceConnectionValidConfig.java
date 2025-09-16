package com.lxy.framework.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Configuration
@Slf4j
public class LettuceConnectionValidConfig implements InitializingBean {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void afterPropertiesSet() {
        if (redisConnectionFactory instanceof LettuceConnectionFactory c) {
            c.setValidateConnection(true);
            log.info("启用Lettuce连接池的物理连接验证 ");
        }
    }
}
