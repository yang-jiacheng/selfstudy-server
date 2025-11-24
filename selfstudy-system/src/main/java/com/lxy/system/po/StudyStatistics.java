package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学习统计
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@TableName(value = "study_statistics", autoResultMap = true)
public class StudyStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 用户id
    private Long userId;

    // 学习日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date studyDay;

    // 学习时长
    private Integer duration;

    public StudyStatistics() {}

    public StudyStatistics(Long userId, Date studyDay, Integer duration) {
        this.userId = userId;
        this.studyDay = studyDay;
        this.duration = duration;
    }

}
