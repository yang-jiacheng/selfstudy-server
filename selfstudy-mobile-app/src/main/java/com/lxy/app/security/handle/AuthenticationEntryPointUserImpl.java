package com.lxy.app.security.handle;

import com.lxy.common.bo.R;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过程的异常会进这里处理
 */

public class AuthenticationEntryPointUserImpl implements AuthenticationEntryPoint {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationEntryPointUserImpl.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("认证过程异常：{}", request.getRequestURI(),authException);
        String result = JsonUtil.toJson(R.fail(1000, "认证失败，请重新登录！"));
        WebUtil.renderString(response,result);
    }
}
