package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * Description: 学习统计
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

@TableName(value = "study_statistics", autoResultMap = true)
public class StudyStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //用户id
    private Integer userId;

    //学习日期
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date studyDay;

    //学习时长
    private Integer duration;

    public StudyStatistics() {
    }

    public StudyStatistics(Integer userId, Date studyDay, Integer duration) {
        this.userId = userId;
        this.studyDay = studyDay;
        this.duration = duration;
    }

    public Integer getId() {
    	return id;
    }

    public void setId(Integer id) {
    	this.id = id;
    }

    public Integer getUserId() {
    	return userId;
    }

    public void setUserId(Integer userId) {
    	this.userId = userId;
    }

    public Date getStudyDay() {
    	return studyDay;
    }

    public void setStudyDay(Date studyDay) {
    	this.studyDay = studyDay;
    }

    public Integer getDuration() {
    	return duration;
    }

    public void setDuration(Integer duration) {
    	this.duration = duration;
    }


    @Override
    public String toString() {
	    return "StudyStatistics{" +
	            ", id=" + id +
	            ", userId=" + userId +
	            ", studyDay=" + studyDay +
	            ", duration=" + duration +
	    "}";
    }

}
