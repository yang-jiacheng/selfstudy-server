package com.lxy.common.aspect;

import cn.hutool.extra.servlet.JakartaServletUtil;
import com.lxy.common.annotation.OperationLog;
import com.lxy.common.security.util.UserIdUtil;
import com.lxy.common.util.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 操作日志切面
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Component
@Aspect
public class OperationLogAspect {

    private final static Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    // 使用 ThreadLocal 存储 userId
    private final ThreadLocal<Integer> userIdHolder = new ThreadLocal<>();

    @Pointcut("@annotation(log)")
    public void logPointCut(OperationLog log) {}

    /**
     * 前置通知：记录方法执行前的日志
     */
    @Before(value = "logPointCut(log)", argNames = "log")
    public void boBefore(OperationLog log) {
        //获取用户id
        int userId = UserIdUtil.getUserId();
        userIdHolder.set(userId);
    }

    /**
     * 后置通知：记录方法执行完后的日志
     * @param joinPoint 切点，获取目标方法的执行结果等信息
     */
    @AfterReturning( pointcut = "logPointCut(log)", returning = "result", argNames = "joinPoint,log,result")
    public void afterMethod(JoinPoint joinPoint, OperationLog log, Object result) {
        handleOperationLog(joinPoint, null,log, result);
    }


    /**
     * 异常通知：记录方法执行时的异常
     * @param joinPoint 切点，获取目标方法的执行信息
     * @param e 异常信息
     */
    @AfterThrowing(pointcut = "logPointCut(log)" ,throwing = "e", argNames = "joinPoint,log,e")
    public void afterThrowingMethod(JoinPoint joinPoint,OperationLog log, Exception e){
        handleOperationLog(joinPoint, e, log,null);
    }

    protected void handleOperationLog(final JoinPoint joinPoint,final Exception e, OperationLog log, Object jsonResult) {
        try {
            Object[] args = joinPoint.getArgs();
            //返回结果
            String resultJson;
            if (jsonResult == null) {
                resultJson = e.getMessage();
            } else if (jsonResult instanceof String){
                resultJson = (String) jsonResult;
            }else {
                resultJson = JsonUtil.toJson(jsonResult);
            }
            HttpServletRequest request = getRequest();
            /*
             * 客户端ip、请求uri、请求方法、模块标题、业务类型
             */
            String clientIP = JakartaServletUtil.getClientIP(request);
            String requestURI = request.getRequestURI();
            String method = request.getMethod();
            String title = log.title();
            Integer businessType = log.businessType().type;
            //用户类型
            Integer userType = log.userType().type;
            //操作状态（0成功 1失败）
            int status = 0;
            if (e != null) {
                status = 1;
            }
            Integer userId = userIdHolder.get();
            //操作日志对象
            logger.info("记录操作日志");
        } catch (Exception ex) {
            logger.error("记录操作日志异常", ex);
        }finally {
            // 清除 ThreadLocal 防止内存泄漏
            logger.info("清除 ThreadLocal !!!");
            userIdHolder.remove();
        }

    }

    /**
     * 获取request对象
     * @author jiacheng yang.
     * @since 2025/3/21 15:17
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletAttributes = (ServletRequestAttributes) attributes;
        if (servletAttributes == null) {
            return null;
        }
        return servletAttributes.getRequest();
    }

}
