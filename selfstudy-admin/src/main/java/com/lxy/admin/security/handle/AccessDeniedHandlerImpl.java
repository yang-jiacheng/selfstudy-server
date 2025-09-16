package com.lxy.admin.security.handle;

import com.lxy.common.domain.R;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 授权过程的异常会进这里处理
 */

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.error("权限不足异常：{}", request.getRequestURI(), accessDeniedException);
        String result = JsonUtil.toJson(R.fail(403, "禁止访问此资源!"));
        WebUtil.renderString(response, result);
    }

}
