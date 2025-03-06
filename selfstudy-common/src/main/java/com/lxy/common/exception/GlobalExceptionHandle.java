package com.lxy.common.exception;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.lxy.common.bo.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/11/18 9:54
 * @Version: 1.0
 */

@RestControllerAdvice
public class GlobalExceptionHandle {

    private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public R<Object> methodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request){
        String msg = StrUtil.format("请求方法不支持 {}, 请求地址为 {}",e.getMethod(),request.getRequestURI());
        LOG.error(msg,e);
        return R.fail(HttpStatus.HTTP_BAD_METHOD,msg);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public R<Object> requestParameterException(MissingServletRequestParameterException e, HttpServletRequest request){
        String msg = StrUtil.format("发生异常_请求参数错误,参数类型为 {} 的必需请求参数 {} 不存在, 请求地址为 {}",e.getParameterType(), e.getParameterName(),request.getRequestURI());
        LOG.error(msg,e);
        return R.fail(HttpStatus.HTTP_BAD_REQUEST,msg);
    }

    @ExceptionHandler({NullPointerException.class})
    public R<Object> handleNullPointerException(NullPointerException e, HttpServletRequest request){
        String msg = StrUtil.format("空指针异常,请求地址为 {}",request.getRequestURI());
        LOG.error(msg,e);
        return R.fail(HttpStatus.HTTP_INTERNAL_ERROR,msg);
    }

}
