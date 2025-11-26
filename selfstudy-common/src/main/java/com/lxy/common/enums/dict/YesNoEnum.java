package com.lxy.common.enums.dict;

/**
 * 是否枚举类
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/11/26 11:09
 */

public enum YesNoEnum {

    YES("1", "是"),
    NO("0", "否");

    public final String value;
    public final String name;

    YesNoEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getNameByValue(String value) {
        for (YesNoEnum e : YesNoEnum.values()) {
            if (e.value.equals(value)) {
                return e.name;
            }
        }
        return "";
    }

    public static String getValueByName(String name) {
        for (YesNoEnum e : YesNoEnum.values()) {
            if (e.name.equals(name)) {
                return e.value;
            }
        }
        return "";
    }

}
