package com.lxy.common.enums;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/19 17:54
 */
public enum StudyStatus {

    LEARNING(1, "自习中"),
    LEAVE(2, "已离开"),
    FINISH(3, "已完成");

    public final Integer type;
    public final String name;

    StudyStatus(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
}
