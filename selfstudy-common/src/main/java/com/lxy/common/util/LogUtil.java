package com.lxy.common.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2023/02/22 16:28
 * @version 1.0
 */
public class LogUtil {

    public static String logOperation(Integer userId, HttpServletRequest request, String requestBody) {
        // 获取访问的地址
        String requestURI = request.getRequestURI();

        // 获取查询参数
        String queryString = request.getParameterMap().entrySet().stream()
                .flatMap(entry -> {
                    String key = entry.getKey();
                    return entry.getValue() != null ?
                            Arrays.stream(entry.getValue()).map(value -> key + "=" + value) :
                            Stream.empty();
                })
                .collect(Collectors.joining("&"));

        // 获取客户端IP
        String clientIP = JakartaServletUtil.getClientIP(request);

        // 构建操作日志信息
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(StrUtil.format("客户端IP: {} 操作记录: userId={}, 请求地址: {}", clientIP, userId, requestURI));

        // 添加查询参数
        if (StrUtil.isNotEmpty(queryString)) {
            logMessage.append(", 参数: ").append(queryString);
        }

        // 判断是否为文件上传，避免打印 `requestBody`
        String contentType = request.getContentType();
        boolean isMultipart = contentType != null && contentType.toLowerCase().startsWith("multipart/");

        if (!isMultipart && StrUtil.isNotEmpty(requestBody)) {
            logMessage.append(", 请求体: \n").append(requestBody);
        }

        return logMessage.toString();
    }

}
