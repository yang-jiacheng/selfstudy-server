package com.lxy.admin.exception;

import cn.hutool.http.HttpStatus;
import com.lxy.common.domain.R;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.LogUtil;
import com.lxy.common.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/11/18 9:54
 * @Version: 1.0
 */

@ControllerAdvice
public class GlobalExceptionHandle {

    private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public R<Object> methodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request){
        String msg = "请求方法不支持 "+e.getMethod();
        String requestURI = request.getRequestURI();
        LOG.error("{}发生异常_请求方法不支持{}",requestURI,e.getMessage());
        return R.fail(HttpStatus.HTTP_BAD_METHOD,msg);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public R<Object> requestParameterException(MissingServletRequestParameterException e, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        LOG.error("{}发生异常_请求参数错误{}",requestURI,e.getMessage());
        e.printStackTrace();
        String msg = "参数类型为 "+e.getParameterType()+" 的必需请求参数 "+ e.getParameterName()+" 不存在";
        return R.fail(HttpStatus.HTTP_BAD_REQUEST,msg);
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public R<Object> handleException(Exception e, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        LOG.error("{}发生异常{}",requestURI,e.getMessage());
        e.printStackTrace();
        String msg = "无法与服务器建立安全连接";
        return R.fail(HttpStatus.HTTP_INTERNAL_ERROR,msg);
    }


}
