package com.lxy.admin.config;


import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: TODO
 * Author: jiacheng yang.
 * Date: 2025/02/21 11:51
 * Version: 1.0
 */

@Configuration
public class FastJson2Config implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 使用 Fastjson2 的 HttpMessageConverter
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        // 可以在 FastJsonConfig 中进行更详细的配置，例如日期格式、序列化特性等
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");

        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        // 设置支持的 MediaType
        converter.setSupportedMediaTypes(getSupportedMediaTypes());
        // 将 FastjsonHttpMessageConverter 放在首位，使其优先被使用
        converters.add(0, converter);
    }

    private List<MediaType> getSupportedMediaTypes() {
        List<MediaType> mediaTypes = new ArrayList<>(1);
        mediaTypes.add(MediaType.APPLICATION_JSON);
        // 可以根据需要添加其他 MediaType，例如 TEXT_PLAIN, APPLICATION_JSON_UTF8 等
        return mediaTypes;
    }

}
