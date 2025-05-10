package com.lxy.common.annotation;

import java.lang.annotation.*;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelHeader {

    //表头
    String title() default "";

    //是否输出到excel
    boolean required() default true;

}
