package com.lxy.system.vo.user;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.annotation.ExcelHeader;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
public class UserExportVO implements Serializable {
    @Serial
    @ExcelHeader(required = false)
    private static final long serialVersionUID = 5375333544880309688L;

    @ExcelHeader(title = "昵称")
    private String name;

    @ExcelHeader(title = "手机号")
    private String phone;

    @ExcelHeader(title = "性别")
    private String gender;

    @ExcelHeader(title = "地址")
    private String address;

    @ExcelHeader(required = false)
    private Integer totalDuration;

    @ExcelHeader(title = "总学习时长")
    private String totalDurationStr;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @ExcelHeader(required = false)
    private Date createTime;
    @ExcelHeader(title = "加入时间")
    private String createTimeStr;
    //注册类型 1。用户注册 2.后台添加
    @ExcelHeader(required = false)
    private Integer registType;
    @ExcelHeader(title = "注册类型")
    private String registTypeStr;

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
        if (totalDuration != null && totalDuration > 0) {
            this.totalDurationStr = totalDuration + "分钟";
        }
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        this.createTimeStr = DateUtil.format(createTime, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
    }

    public void setRegistType(Integer registType) {
        this.registType = registType;
        if (registType != null) {
            if (registType == 1) {
                this.registTypeStr = "用户注册";
            } else if (registType == 2) {
                this.registTypeStr = "后台添加";
            }
        }
    }
}
