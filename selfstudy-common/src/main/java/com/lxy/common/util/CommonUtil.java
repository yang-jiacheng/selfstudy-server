package com.lxy.common.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/05 19:55
 * @version 1.0
 */
public class CommonUtil {

    public static String hopeGc(){
        String date = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
        String msg = date + "_希望进行垃圾回收";
        System.gc();
        return msg;
    }


}
