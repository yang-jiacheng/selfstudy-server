package com.lxy.common.enums;

/**
 * 操作日志业务类型
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

public enum LogBusinessType {

    OTHER(0, "其他"), INSERT(1, "新增"), UPDATE(2, "修改"), DELETE(3, "删除"), IMPORT(4, "导入"), EXPORT(5, "导出");

    public final Integer type;
    public final String name;

    LogBusinessType(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(Integer type) {
        for (LogBusinessType businessType : LogBusinessType.values()) {
            if (businessType.type.equals(type)) {
                return businessType.name;
            }
        }
        return "-";
    }

}
