package com.lxy.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 学习记录DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/24 16:36
 */

@Data
public class StudyRecordDTO implements Serializable {
    private static final long serialVersionUID = 2247994618565339738L;

    /*
     * 自习室id
     */
    @NotNull
    private Long catalogId;

    /*
     * 标签
     */
    @NotNull
    private String tag;

    /*
     * 座位号
     */
    @NotNull
    private Integer seat;

    /*
     * 计时方式：1正计时 2倒计时
     */
    @NotNull
    private Integer timingMode;

    /*
     * 设置的自习时长，单位分钟（仅在倒计时有）
     */
    private Integer settingDuration;

}
