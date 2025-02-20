package com.lxy.common.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/24 16:36
 * @Version: 1.0
 */

public class StudyRecordDTO implements Serializable {
    private static final long serialVersionUID = 2247994618565339738L;

    /*
     * 自习室id
     */
    @NotNull
    private Integer catalogId;

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

    public Integer getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Integer catalogId) {
        this.catalogId = catalogId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public Integer getTimingMode() {
        return timingMode;
    }

    public void setTimingMode(Integer timingMode) {
        this.timingMode = timingMode;
    }

    public Integer getSettingDuration() {
        return settingDuration;
    }

    public void setSettingDuration(Integer settingDuration) {
        this.settingDuration = settingDuration;
    }

    @Override
    public String toString() {
        return "StudyRecordDTO{" +
                "catalogId=" + catalogId +
                ", tag='" + tag + '\'' +
                ", seat=" + seat +
                ", timingMode=" + timingMode +
                ", settingDuration=" + settingDuration +
                '}';
    }
}
