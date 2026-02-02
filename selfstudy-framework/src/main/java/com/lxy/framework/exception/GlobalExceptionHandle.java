package com.lxy.framework.exception;

import cn.hutool.core.util.StrUtil;
import com.lxy.common.exception.ServiceException;
import com.lxy.common.model.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/11/18 9:54
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public R<Object> methodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String msg = StrUtil.format("请求方法不支持 {}, 请求地址为 {}", e.getMethod(), request.getRequestURI());
        log.error(msg, e);
        return R.fail(msg);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public R<Object> requestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String msg = StrUtil.format("请求参数错误,参数类型为 {} 的必需请求参数 {} 不存在, 请求地址为 {}", e.getParameterType(),
            e.getParameterName(), request.getRequestURI());
        log.error(msg, e);
        return R.fail(msg);
    }

    /**
     * 请求体验证异常处理（JSR303校验失败）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Object> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        // 获取校验错误信息
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder("参数校验失败：");
        for (FieldError error : bindingResult.getFieldErrors()) {
            sb.append(StrUtil.format("[字段: {} 错误信息: {}] ", error.getField(), error.getDefaultMessage()));
        }
        String msg = StrUtil.format("{}，请求地址为 {}", sb.toString(), request.getRequestURI());
        log.error(msg, e);
        return R.fail(msg);
    }

    @ExceptionHandler({NullPointerException.class})
    public R<Object> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        String msg = StrUtil.format("空指针错误,请求地址为 {}", request.getRequestURI());
        log.error(msg, e);
        return R.fail(msg);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public R<Object> handleAuthCredentialsMissing(AuthenticationCredentialsNotFoundException ex,
        HttpServletRequest request) {
        String msg = StrUtil.format("未认证访问受权限保护的资源 {}", request.getRequestURI());
        log.error(msg, ex);
        return R.fail(HttpStatus.FORBIDDEN.value(), "禁止访问此资源!");
    }

    @ExceptionHandler(ServiceException.class)
    public R<Object> handleServiceException(ServiceException exception, HttpServletRequest request) {
        String msg = StrUtil.format("业务异常: {}, 请求地址为 {}", exception.getMessage(), request.getRequestURI());
        log.error(msg, exception);
        return R.fail(exception.getMessage());
    }

}
