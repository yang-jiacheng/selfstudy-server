package com.lxy.framework.config;

import com.lxy.common.properties.CustomProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/11/10 14:24
 * @Version: 1.0
 */

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 未使用OSS时，图片路径映射到服务器的文件
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+ CustomProperties.uploadPath);
    }



}
