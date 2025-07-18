package com.lxy.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * XXL-Job配置属性类
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/7/18 14:16
 */

@Data
@Component
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    /**
     * 是否启用xxl-job
     */
    private boolean enabled = false;

    /**
     * 管理端配置
     */
    private Admin admin = new Admin();

    /**
     * 执行器配置
     */
    private Executor executor = new Executor();

    @Data
    public static class Admin {
        /**
         * 管理端地址
         */
        private String addresses;

        /**
         * 访问令牌
         */
        private String accessToken;

        /**
         * 超时时间
         */
        private int timeout = 3;
    }

    @Data
    public static class Executor {
        /**
         * 应用名称
         */
        private String appname;

        /**
         * 执行器IP
         */
        private String ip;

        /**
         * 执行器端口
         */
        private int port;

        /**
         * 日志路径
         */
        private String logpath;

        /**
         * 日志保留天数
         */
        private int logretentiondays = 30;
    }
}

