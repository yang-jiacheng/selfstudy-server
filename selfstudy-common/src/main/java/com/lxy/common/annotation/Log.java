package com.lxy.common.annotation;

import com.lxy.common.enums.dict.LogBusinessType;
import com.lxy.common.enums.dict.UserType;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块名称
     */
    String title() default "";


    /**
     * 业务类型
     */
    LogBusinessType businessType() default LogBusinessType.OTHER;

    /**
     * 用户类型
     */
    UserType userType() default UserType.USER;

}
