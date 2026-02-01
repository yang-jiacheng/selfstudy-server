package com.lxy.common.enums.dict;

import lombok.Getter;

/**
 * 操作日志用户类型
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Getter
public enum LogUserType {

    ADMIN(0, "后台用户"), USER(1, "APP用户");

    private final Integer type;
    private final String name;

    LogUserType(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(Integer type) {
        for (LogUserType logUserType : LogUserType.values()) {
            if (logUserType.getType().equals(type)) {
                return logUserType.name;
            }
        }
        return "-";
    }

}
