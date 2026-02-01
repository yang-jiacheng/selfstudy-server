package com.lxy.common.enums.dict;

import lombok.Getter;

/**
 * 是或否 枚举类
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/11/26 11:09
 */

@Getter
public enum YesOrNo {

    YES("1", "是"), NO("0", "否");

    private final String value;
    private final String name;

    YesOrNo(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getNameByValue(String value) {
        for (YesOrNo e : YesOrNo.values()) {
            if (e.getValue().equals(value)) {
                return e.name;
            }
        }
        return "";
    }

    public static String getValueByName(String name) {
        for (YesOrNo e : YesOrNo.values()) {
            if (e.getName().equals(name)) {
                return e.value;
            }
        }
        return "";
    }

}
