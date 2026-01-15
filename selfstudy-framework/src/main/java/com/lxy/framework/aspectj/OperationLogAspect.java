package com.lxy.framework.aspectj;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.lxy.common.annotation.Log;
import com.lxy.common.util.JsonUtil;
import com.lxy.framework.event.OperationLogEvent;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.po.OperationLog;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;
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
@Slf4j
public class OperationLogAspect {

    private final ThreadLocal<Long> timeHolder = new ThreadLocal<>();
    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 获取request对象
     *
     * @author jiacheng yang.
     * @since 2025/3/21 15:17
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletAttributes = (ServletRequestAttributes)attributes;
        if (servletAttributes == null) {
            return null;
        }
        return servletAttributes.getRequest();
    }

    /**
     * 将方法参数拼接成可用于日志的字符串
     */
    public static String buildParamString(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }

        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            String param = "";

            if (arg instanceof String || arg instanceof Number || arg instanceof Boolean) {
                param = arg.toString();
            } else if (arg instanceof HttpServletRequest) {
                param = "HttpServletRequest";
            } else if (arg instanceof HttpServletResponse) {
                param = "HttpServletResponse";
            } else {
                param = JsonUtil.toJson(arg);
            }

            params.append(param).append(", ");
        }

        // 去掉最后多余的逗号和空格
        if (params.length() >= 2) {
            params.setLength(params.length() - 2);
        }

        return params.toString();
    }

    @Pointcut("@annotation(operationLog)")
    public void logPointCut(Log operationLog) {}

    /**
     * 前置通知：记录方法执行前的日志
     */
    @Before(value = "logPointCut(operationLog)", argNames = "operationLog")
    public void boBefore(Log operationLog) {
        timeHolder.set(System.currentTimeMillis());
    }

    /**
     * 后置通知：记录方法执行完后的日志
     *
     * @param joinPoint 切点，获取目标方法的执行结果等信息
     */
    @AfterReturning(pointcut = "logPointCut(operationLog)", returning = "result",
        argNames = "joinPoint,operationLog,result")
    public void afterMethod(JoinPoint joinPoint, Log operationLog, Object result) {
        handleOperationLog(joinPoint, null, operationLog, result);
    }

    /**
     * 异常通知：记录方法执行时的异常
     *
     * @param joinPoint 切点，获取目标方法的执行信息
     * @param e 异常信息
     */
    @AfterThrowing(pointcut = "logPointCut(operationLog)", throwing = "e", argNames = "joinPoint,operationLog,e")
    public void afterThrowingMethod(JoinPoint joinPoint, Log operationLog, Exception e) {
        handleOperationLog(joinPoint, e, operationLog, null);
    }

    protected void handleOperationLog(final JoinPoint joinPoint, final Exception e, Log operationLog,
        Object jsonResult) {
        try {
            Object[] args = joinPoint.getArgs();
            String param = buildParamString(args);
            // 返回结果
            String resultJson = null;
            if (jsonResult == null) {
                resultJson = (e != null ? e.getMessage() : "");
            } else if (jsonResult instanceof String) {
                resultJson = (String)jsonResult;
            } else {
                resultJson = JsonUtil.toJson(jsonResult);
            }
            HttpServletRequest request = getRequest();
            /*
             * 客户端ip、请求uri、请求方法、模块标题、业务类型
             */
            String clientIP = JakartaServletUtil.getClientIP(request);
            String requestURI = request.getRequestURI();
            String method = request.getMethod();
            String title = operationLog.title();
            Integer businessType = operationLog.businessType().type;
            // 用户类型
            Integer userType = operationLog.userType().type;
            // 操作状态（0成功 1失败）
            int status = 0;
            if (e != null) {
                status = 1;
            }
            long userId = UserIdUtil.getUserId();
            Long startTime = timeHolder.get();
            Long endTime = System.currentTimeMillis();
            String durationStr = DateUtil.formatBetween(endTime - startTime, BetweenFormatter.Level.MILLISECOND);
            // 操作日志对象
            OperationLog operLog = new OperationLog(title, businessType, userType, userId, requestURI, method, param,
                resultJson, clientIP, status, durationStr);
            // 发布事件
            eventPublisher.publishEvent(new OperationLogEvent(this, operLog));
        } catch (Exception ex) {
            log.error("记录操作日志异常", ex);
        } finally {
            // 清除 ThreadLocal 防止内存泄漏
            log.info("清除 ThreadLocal !!!");
            timeHolder.remove();
        }

    }

}
