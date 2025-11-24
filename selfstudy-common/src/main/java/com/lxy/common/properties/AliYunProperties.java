package com.lxy.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 阿里云配置
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Component
public class AliYunProperties {

    public static String accessKeyId;

    public static String accessKeySecret;

    public static Boolean ossEnabled;

    public static String ossPath;

    public static String ossBucket;

    public static String ossEndpoint;

    @Value("${ali.accesskey.id}")
    public void setAccessKeyId(String accessKeyId) {
        AliYunProperties.accessKeyId = accessKeyId;
    }

    @Value("${ali.accesskey.secret}")
    public void setAccessKeySecret(String accessKeySecret) {
        AliYunProperties.accessKeySecret = accessKeySecret;
    }

    @Value("${oss.config.enabled}")
    public void setOssEnabled(Boolean ossEnabled) {
        AliYunProperties.ossEnabled = ossEnabled;
    }

    @Value("${oss.config.path}")
    public void setOssPath(String ossPath) {
        AliYunProperties.ossPath = ossPath;
    }

    @Value("${oss.config.bucket}")
    public void setOssBucket(String ossBucket) {
        AliYunProperties.ossBucket = ossBucket;
    }

    @Value("${oss.config.endpoint}")
    public void setOssEndpoint(String ossEndpoint) {
        AliYunProperties.ossEndpoint = ossEndpoint;
    }
}
