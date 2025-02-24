package com.lxy.admin.security.handle;

import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.WebUtil;
import com.lxy.common.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权过程的异常会进这里处理
 */

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        ResultVO result = new ResultVO(HttpStatus.FORBIDDEN.value(),"您的权限不足");
//        String json = JsonUtil.toJson(result);
//        //处理异常
//        WebUtil.renderString(response,json);
        logger.error("权限不足：{}",request.getRequestURI());
        WebUtil.renderRedirect(response,"/error403");
    }

}
