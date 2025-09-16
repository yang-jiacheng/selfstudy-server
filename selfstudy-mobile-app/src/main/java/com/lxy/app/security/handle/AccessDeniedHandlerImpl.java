package com.lxy.app.security.handle;

import cn.hutool.http.HttpStatus;
import com.lxy.common.domain.R;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 授权过程的异常会进这里处理
 */

@Deprecated
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        R<Object> result = R.fail(HttpStatus.HTTP_FORBIDDEN, "您的权限不足");
        String json = JsonUtil.toJson(result);
        //处理异常
        WebUtil.renderString(response, json);
    }

}
