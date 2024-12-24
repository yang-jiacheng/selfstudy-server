package com.lxy.common.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/22 16:28
 * @Version: 1.0
 */
public class LogUtil {

    private final static Logger LOG = LoggerFactory.getLogger(LogUtil.class);

    public static String logOperation(Integer userId, HttpServletRequest request){

        // 访问的地址
        String requestURI = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder queryString = new StringBuilder();
        for (String key2 : parameterMap.keySet()) {
            String[] values = parameterMap.get(key2);
            for (String value : values) {
                queryString.append(key2).append("=").append(value).append("&");
            }
        }
        // 去掉最后一个空格
        if(queryString.length()>0) {
            queryString = new StringBuilder(queryString.substring(0, queryString.length() - 1));
        }
        String clientIP = ServletUtil.getClientIP(request);
        String msg = clientIP+"操作记录：userId--"+userId+"--请求地址--"+requestURI+"--参数--"+queryString;
        return msg;
    }

}
