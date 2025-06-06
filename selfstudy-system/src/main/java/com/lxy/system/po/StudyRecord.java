package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * Description: 学习记录
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

@TableName(value = "study_record", autoResultMap = true)
public class StudyRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //用户id
    private Integer userId;

    //图书馆id
    private Integer classifyId;

    //节点id
    private Integer catalogId;

    //学习标签
    private String tag;

    //座位号
    private Integer seat;

    //计时方式：1正计时 2倒计时
    private Integer timingMode;

    //设置的自习时长，单位分钟（仅在倒计时有）
    private Integer settingDuration;

    //实际学习时长，单位分钟
    private Integer actualDuration;

    //状态（1自习中 2离开 3已完成）
    private Integer status;

    //学习笔记文字
    private String noteContent;

    //学习笔记图片
    private String notePath;

    //是否有笔记 0否 1是
    private Integer noteStatus;

    //开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date startTime;

    //修改时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date updateTime;

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

    public Integer getClassifyId() {
    	return classifyId;
    }

    public void setClassifyId(Integer classifyId) {
    	this.classifyId = classifyId;
    }

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

    public Integer getActualDuration() {
    	return actualDuration;
    }

    public void setActualDuration(Integer actualDuration) {
    	this.actualDuration = actualDuration;
    }

    public Integer getStatus() {
    	return status;
    }

    public void setStatus(Integer status) {
    	this.status = status;
    }

    public String getNoteContent() {
    	return noteContent;
    }

    public void setNoteContent(String noteContent) {
    	this.noteContent = noteContent;
    }

    public String getNotePath() {
    	return notePath;
    }

    public void setNotePath(String notePath) {
    	this.notePath = notePath;
    }

    public Integer getNoteStatus() {
    	return noteStatus;
    }

    public void setNoteStatus(Integer noteStatus) {
    	this.noteStatus = noteStatus;
    }

    public Date getStartTime() {
    	return startTime;
    }

    public void setStartTime(Date startTime) {
    	this.startTime = startTime;
    }

    public Date getUpdateTime() {
    	return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
    	this.updateTime = updateTime;
    }


    @Override
    public String toString() {
	    return "StudyRecord{" +
	            ", id=" + id +
	            ", userId=" + userId +
	            ", classifyId=" + classifyId +
	            ", catalogId=" + catalogId +
	            ", tag=" + tag +
	            ", seat=" + seat +
	            ", timingMode=" + timingMode +
	            ", settingDuration=" + settingDuration +
	            ", actualDuration=" + actualDuration +
	            ", status=" + status +
	            ", noteContent=" + noteContent +
	            ", notePath=" + notePath +
	            ", noteStatus=" + noteStatus +
	            ", startTime=" + startTime +
	            ", updateTime=" + updateTime +
	    "}";
    }

}
