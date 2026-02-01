package com.lxy.common.enums.dict;

import lombok.Getter;

/**
 * 操作日志业务类型
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Getter
public enum LogBusinessType {

    OTHER("0", "其他"), INSERT("1", "新增"), UPDATE("2", "修改"), DELETE("3", "删除"), IMPORT("4", "导入"), EXPORT("5", "导出");

    private final String type;
    private final String name;

    LogBusinessType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(String type) {
        for (LogBusinessType businessType : LogBusinessType.values()) {
            if (businessType.getType().equals(type)) {
                return businessType.name;
            }
        }
        return "-";
    }

}
