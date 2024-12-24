package com.lxy.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/24 16:36
 * @Version: 1.0
 */

@ApiModel(value ="学习记录数据传输对象")
public class StudyRecordDTO implements Serializable {
    private static final long serialVersionUID = 2247994618565339738L;

    @ApiModelProperty(value = "自习室id",required = true)
    private Integer catalogId;

    @ApiModelProperty(value = "学习标签",required = true)
    private String tag;

    @ApiModelProperty(value = "座位号",required = true)
    private Integer seat;

    @ApiModelProperty(value = "计时方式：1正计时 2倒计时",example = "1",required = true)
    private Integer timingMode;

    @ApiModelProperty(value = "设置的自习时长，单位分钟（仅在倒计时有）")
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
