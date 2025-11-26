package com.lxy.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学习记录vo
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/24 16:10
 */

@Data
public class StudyRecordVO implements Serializable {
    private static final long serialVersionUID = -559230604195324125L;

    private Long id;

    // 用户id
    private Long userId;

    // 图书馆id
    private Long classifyId;

    // 节点id
    private Long catalogId;

    // 学习标签
    private String tag;

    // 座位号
    private Integer seat;

    // 计时方式：1正计时 2倒计时
    private Integer timingMode;

    // 设置的自习时长，单位分钟（仅在倒计时有）
    private Integer settingDuration;

    // 实际学习时长，单位分钟
    private Integer actualDuration;

    // 状态（1自习中 2离开 3已完成）
    private Integer status;

    // 学习笔记文字
    private String noteContent;

    // 学习笔记图片
    private String notePath;

    // 是否有笔记 0否 1是
    private Integer noteStatus;

    // 开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date startTime;

    // 修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 用户昵称
     */
    private String name;

    private String phone;

    /**
     * 用户头像
     */
    private String profilePath;

    /**
     * 图书馆名称
     */
    private String classifyName;

    /**
     * 自习室名称
     */
    private String catalogName;

    private String parentCatalogName;

}
