package com.lxy.common.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * Description: 意见反馈
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

public class Feedback implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //用户id
    private Integer userId;

    //反馈内容
    private String content;

    //反馈图片
    private String pic;

    //回复内容
    private String reply;

    //回复人
    private Integer adminId;

    //创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date createTime;

    //回复时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date replyTime;

    //用户是否可见 1可见 2不可见
    private Integer status;

    //回复状态 0未回复 1已回复
    private Integer replyStatus;

    public Feedback() {
    }

    public Feedback(Integer userId, String content, String pic, Date createTime, Integer status, Integer replyStatus) {
        this.userId = userId;
        this.content = content;
        this.pic = pic;
        this.createTime = createTime;
        this.status = status;
        this.replyStatus = replyStatus;
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

    public String getContent() {
    	return content;
    }

    public void setContent(String content) {
    	this.content = content;
    }

    public String getPic() {
    	return pic;
    }

    public void setPic(String pic) {
    	this.pic = pic;
    }

    public String getReply() {
    	return reply;
    }

    public void setReply(String reply) {
    	this.reply = reply;
    }

    public Integer getAdminId() {
    	return adminId;
    }

    public void setAdminId(Integer adminId) {
    	this.adminId = adminId;
    }

    public Date getCreateTime() {
    	return createTime;
    }

    public void setCreateTime(Date createTime) {
    	this.createTime = createTime;
    }

    public Date getReplyTime() {
    	return replyTime;
    }

    public void setReplyTime(Date replyTime) {
    	this.replyTime = replyTime;
    }

    public Integer getStatus() {
    	return status;
    }

    public void setStatus(Integer status) {
    	this.status = status;
    }

    public Integer getReplyStatus() {
    	return replyStatus;
    }

    public void setReplyStatus(Integer replyStatus) {
    	this.replyStatus = replyStatus;
    }


    @Override
    public String toString() {
	    return "Feedback{" +
	            ", id=" + id +
	            ", userId=" + userId +
	            ", content=" + content +
	            ", pic=" + pic +
	            ", reply=" + reply +
	            ", adminId=" + adminId +
	            ", createTime=" + createTime +
	            ", replyTime=" + replyTime +
	            ", status=" + status +
	            ", replyStatus=" + replyStatus +
	    "}";
    }

}
