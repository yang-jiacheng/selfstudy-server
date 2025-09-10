package com.lxy.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2023/02/22 16:28
 */
public class LogUtil {

    public static String logOperation(Integer userId, HttpServletRequest request, String requestBody) {
        // 获取访问的地址
        String requestURI = request.getRequestURI();


        // 获取客户端IP
        String clientIP = JakartaServletUtil.getClientIP(request);

        // 构建操作日志信息


//        // 获取查询参数
//        String queryString = request.getParameterMap().entrySet().stream()
//                .flatMap(entry -> {
//                    String key = entry.getKey();
//                    return entry.getValue() != null ?
//                            Arrays.stream(entry.getValue()).map(value -> key + "=" + value) :
//                            Stream.empty();
//                })
//                .collect(Collectors.joining("&"));
//        // 添加查询参数
//        if (StrUtil.isNotEmpty(queryString)) {
//            logMessage.append(", 参数: ").append(queryString);
//        }
//
//        // 判断是否为文件上传，避免打印 `requestBody`
//        String contentType = request.getContentType();
//        boolean isMultipart = contentType != null && contentType.toLowerCase().startsWith("multipart/");
//
//        if (!isMultipart && StrUtil.isNotEmpty(requestBody)) {
//            logMessage.append(", 请求体: \n").append(requestBody);
//        }

        return StrUtil.format("客户端IP: {} 操作记录: userId={}, 请求地址: {}", clientIP, userId, requestURI)
//        // 获取查询参数
//        String queryString = request.getParameterMap().entrySet().stream()
//                .flatMap(entry -> {
//                    String key = entry.getKey();
//                    return entry.getValue() != null ?
//                            Arrays.stream(entry.getValue()).map(value -> key + "=" + value) :
//                            Stream.empty();
//                })
//                .collect(Collectors.joining("&"));
//        // 添加查询参数
//        if (StrUtil.isNotEmpty(queryString)) {
//            logMessage.append(", 参数: ").append(queryString);
//        }
//
//        // 判断是否为文件上传，避免打印 `requestBody`
//        String contentType = request.getContentType();
//        boolean isMultipart = contentType != null && contentType.toLowerCase().startsWith("multipart/");
//
//        if (!isMultipart && StrUtil.isNotEmpty(requestBody)) {
//            logMessage.append(", 请求体: \n").append(requestBody);
//        }
                ;
    }

}
