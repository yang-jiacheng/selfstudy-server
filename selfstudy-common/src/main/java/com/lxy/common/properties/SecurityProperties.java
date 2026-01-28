package com.lxy.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Security配置属性
 *
 * @author jiacheng yang.
 * @version 1.0.0
 * @since 2026/01/29 00:18
 */
@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * 需要认证的URL路径
     */
    private String[] authUrl;

    /**
     * 允许匿名访问的URL路径
     */
    private String[] permitUrl;

}
