package com.lxy.common.constant;

/**
 * redis的所有key
 * @author jiacheng yang.
 * @since 2022/04/25 16:45
 * @version 1.0
 */
public class RedisKeyConstant {

    /**
     * 用户登录状态
     */
    private static final String LOGIN_STATUS = "LOGIN_STATUS";

    /**
     * admin登录状态
     */
    private static final String ADMIN_LOGIN_STATUS = "ADMIN_LOGIN_STATUS";

    /**
     * 业务配置
     */
    private static final String BUSINESS_CONFIG = "BusinessConfig:";

    /**
     * 验证码次数
     */
    private static final String PHONE_SMS = "sms_number:";

    /**
     * 图书馆
     */
    private static final String CLASSIFY_LIST = "ClassifyList";

    /**
     * 用户信息
     */
    private static final String USER_INFO = "userInfo:";

    /**
     * 排行榜
     */
    private static final String RANKINGS = "Rankings";


    private static final String CODE_KEY = "kaptchaCode:";

    private static final String MATH_CODE_KEY = "kaptchaCodeMath:";

    public static String getLoginStatus(Integer userId) {
        return LOGIN_STATUS + ":user_" + userId;
    }

    public static String getAdminLoginStatus(Integer adminId) {
        return ADMIN_LOGIN_STATUS + ":admin_" + adminId;
    }

    public static String getBusinessConfig(String key){
        return BUSINESS_CONFIG+key;
    }

    public static String getPhoneSms(String phone){
        return PHONE_SMS+ "phone_"+ phone;
    }

    public static String getClassify(){
        return CLASSIFY_LIST;
    }

    public static String getUserInfo(Integer userId){
        return USER_INFO + "userId_" + userId;
    }

    public static String getRankings(){
        return RANKINGS;
    }

    public static String getCodeKey(String uuid){
        return CODE_KEY + "uuid_" +  uuid;
    }

    public static String getMathCodeKey(String uuid){
        return MATH_CODE_KEY + "uuid_" + uuid;
    }
}
