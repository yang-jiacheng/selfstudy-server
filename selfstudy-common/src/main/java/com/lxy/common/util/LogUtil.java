package com.lxy.common.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/22 16:28
 * @Version: 1.0
 */
public class LogUtil {

    public static String logOperation(Integer userId, HttpServletRequest request, String requestBody) {
        // 获取访问的地址
        String requestURI = request.getRequestURI();

        // 拼接查询参数
        String queryString = request.getParameterMap().entrySet().stream()
                .flatMap(entry -> {
                    String key = entry.getKey();
                    return entry.getValue() != null ?
                            java.util.Arrays.stream(entry.getValue()).map(value -> key + "=" + value) :
                            java.util.stream.Stream.empty();
                })
                .collect(Collectors.joining("&"));

        // 获取客户端IP
        
        String clientIP = ServletUtil.getClientIP(request);
        String format = StrUtil.format("客户端ip: {} 操作记录: userId={}, 请求地址: {}, 参数: {}",
                clientIP, userId, requestURI, queryString);
        if(StrUtil.isNotEmpty(requestBody)){
            format  = format + ", 请求体: \n" + requestBody;
        }
        // 构建操作日志信息
        return format;
    }

}
