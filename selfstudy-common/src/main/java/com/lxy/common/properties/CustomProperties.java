package com.lxy.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 自定义配置
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/11/10 17:20
 */
@Component
public class CustomProperties {

    public static String filePath;

    public static String uploadPath;

    public static String contentPath;

    public static String hostName;

    public static Integer port;

    public static String activeProfile;


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

}
