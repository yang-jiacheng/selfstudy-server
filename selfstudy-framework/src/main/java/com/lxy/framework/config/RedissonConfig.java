package com.lxy.framework.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Slf4j
@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.database}")
    private Integer database;

    @Value("${spring.application.name}")
    private String clientName;

    @PostConstruct
    public void validate() {
        log.info("Redisson 配置初始化 -> host: {}, port: {}, db: {}, clientName: {}", host, port, database, clientName);
    }

    @Bean
    public RedissonClient redissonClient() {
        // 创建配置对象
        Config config = new Config();

        // 配置单节点 Redis
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)  // 必须指定 redis://
                .setPassword(password)                  // 可选：Redis 密码
                .setDatabase(database)                               // 可选：选择数据库（默认 0）
                .setClientName(clientName)             // 可选：客户端名称，便于在 Redis 监控中识别

                // 连接池与最小空闲连接
                .setConnectionPoolSize(64)                    // 连接池大小（默认 64）
                .setConnectionMinimumIdleSize(10)             // 最小空闲连接数（默认 10）

                // 超时设置
                .setConnectTimeout(10000)                     // 连接超时时间（毫秒，默认 10000）
                .setTimeout(3000)                             // 命令等待响应超时时间（默认 3000）
                // 重试策略
                .setRetryAttempts(3)                          // 命令失败重试次数（默认 3）
                .setRetryInterval(1500)                       // 命令重试时间间隔（默认 1500 毫秒）
                // DNS 相关
                .setDnsMonitoringInterval(5000);              // DNS监控间隔（单位：毫秒，默认 5000）

        // 创建 RedissonClient 实例
        return Redisson.create(config);
    }

}
