package com.lxy.common.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/11/10 17:20
 * @Version: 1.0
 */
@Component
public class CustomProperties {

    public static String filePath;

    public static String uploadPath;

    public static String contentPath;

    public static String hostName;

    public static Integer port;

    public static String activeProfile;

    public static String accessKeyId;

    public static String accessKeySecret;

    public static Boolean ossEnabled;

    public static String ossPath;

    public static String ossBucket;

    public static String ossEndpoint;

    @Value("${sourse.config.file}")
    public void setFilePath(String filePath) {
        CustomProperties.filePath = filePath;
    }

    @Value("${sourse.config.upload}")
    public void setUploadPath(String uploadPath) {
        CustomProperties.uploadPath = uploadPath;
    }

    @Value("${host.name}")
    public void setHostName(String hostName) {
        CustomProperties.hostName = hostName;
    }

    @Value("${server.servlet.context-path}")
    public void setContentPath(String contentPath) {
        CustomProperties.contentPath = contentPath;
    }

    @Value("${server.port}")
    public void setPort(Integer port) {
        CustomProperties.port = port;
    }

    @Value("${spring.profiles.active}")
    public void setActiveProfile(String activeProfile) {
        CustomProperties.activeProfile = activeProfile;
    }

    @Value("${ali.accesskey.id}")
    public void setAccessKeyId(String accessKeyId) {
        CustomProperties.accessKeyId = accessKeyId;
    }

    @Value("${ali.accesskey.secret}")
    public void setAccessKeySecret(String accessKeySecret) {
        CustomProperties.accessKeySecret = accessKeySecret;
    }

    @Value("${oss.config.enabled}")
    public void setOssEnabled(Boolean ossEnabled) {
        CustomProperties.ossEnabled = ossEnabled;
    }

    @Value("${oss.config.path}")
    public void setOssPath(String ossPath) {
        CustomProperties.ossPath = ossPath;
    }

    @Value("${oss.config.bucket}")
    public void setOssBucket(String ossBucket) {
        CustomProperties.ossBucket = ossBucket;
    }

    @Value("${oss.config.endpoint}")
    public void setOssEndpoint(String ossEndpoint) {
        CustomProperties.ossEndpoint = ossEndpoint;
    }
}
