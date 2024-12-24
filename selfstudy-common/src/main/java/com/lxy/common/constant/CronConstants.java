package com.lxy.common.constant;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/23 18:04
 * @Version: 1.0
 */
public class CronConstants {

    /**
     * 每分钟第1秒
     */
    public final static String MINUTES = "1 * * * * ?";

    /**
     * 每天0点第5秒
     */
    public final static String DAY = "5 0 0 * * ?";

    /**
     * 每周一0点第15秒
     */
    public final static String WEEK = "15 0 0 ? * MON";

    /**
     * 每月1号0点第25秒
     */
    public final static String MONTH = "25 0 0 1 * ?";

}
