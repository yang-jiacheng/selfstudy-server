package com.lxy.common.enums.dict;

import lombok.Getter;

/**
 * 用户类型
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Getter
public enum UserType {

    ADMIN("0", "后台用户"), USER("1", "APP用户");

    private final String type;
    private final String name;

    UserType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(String type) {
        for (UserType logUserType : UserType.values()) {
            if (logUserType.getType().equals(type)) {
                return logUserType.name;
            }
        }
        return "-";
    }

}
