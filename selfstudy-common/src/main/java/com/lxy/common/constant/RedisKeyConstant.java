package com.lxy.common.constant;

/**
 * redis的所有key
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/04/25 16:45
 */
public class RedisKeyConstant {

    /**
     * 用户登录状态
     */
    private static final String LOGIN_STATUS = "LOGIN_STATUS";

    /**
     * admin双令牌会话 - 使用有序集合存储
     */
    private static final String ADMIN_LOGIN_STATUS = "ADMIN_LOGIN_STATUS";

    /**
     * admin权限
     */
    private static final String ADMIN_INFO = "ADMIN_INFO:";

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

    public static String getLoginStatus(Long userId) {
        return LOGIN_STATUS + ":user_" + userId;
    }

    /**
     * 获取管理员双令牌会话Redis键
     */
    public static String getAdminDualToken(Long adminId) {
        return ADMIN_LOGIN_STATUS + ":admin_" + adminId;
    }

    public static String getBusinessConfig(String key) {
        return BUSINESS_CONFIG + key;
    }

    public static String getPhoneSms(String phone) {
        return PHONE_SMS + "phone_" + phone;
    }

    public static String getClassify() {
        return CLASSIFY_LIST;
    }

    public static String getUserInfo(Long userId) {
        return USER_INFO + "userId_" + userId;
    }

    public static String getRankings() {
        return RANKINGS;
    }

    public static String getCodeKey(String uuid) {
        return CODE_KEY + "uuid_" + uuid;
    }

    public static String getMathCodeKey(String uuid) {
        return MATH_CODE_KEY + "uuid_" + uuid;
    }

    public static String getAdminInfo(Long adminId) {
        return ADMIN_INFO + "admin_" + adminId;
    }
}
