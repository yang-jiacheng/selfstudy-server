package com.lxy.admin.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * Description: 操作日志
 * author: jiacheng yang.
 * Date: 2025-03-21
 */

@TableName("operation_log")
public class OperationLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //模块标题
    private String title;

    //业务类型（0其它 1新增 2修改 3删除 4导入 5导出）
    private Integer businessType;

    //1后台用户 2APP用户
    private String userType;

    //用户id
    private Integer userId;

    //用户昵称
    private String userName;

    //用户手机号
    private String phone;

    //请求地址
    private String requestUrl;

    //请求方式
    private String requestMethod;

    //请求参数
    private String requestParams;

    //请求结果
    private String requestResult;

    //客端ip
    private String clientIp;

    //操作状态（0成功 1失败）
    private Integer status;

    //请求时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date createTime;

    public Integer getId() {
    	return id;
    }

    public void setId(Integer id) {
    	this.id = id;
    }

    public String getTitle() {
    	return title;
    }

    public void setTitle(String title) {
    	this.title = title;
    }

    public Integer getBusinessType() {
    	return businessType;
    }

    public void setBusinessType(Integer businessType) {
    	this.businessType = businessType;
    }

    public String getUserType() {
    	return userType;
    }

    public void setUserType(String userType) {
    	this.userType = userType;
    }

    public Integer getUserId() {
    	return userId;
    }

    public void setUserId(Integer userId) {
    	this.userId = userId;
    }

    public String getUserName() {
    	return userName;
    }

    public void setUserName(String userName) {
    	this.userName = userName;
    }

    public String getPhone() {
    	return phone;
    }

    public void setPhone(String phone) {
    	this.phone = phone;
    }

    public String getRequestUrl() {
    	return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
    	this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
    	return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
    	this.requestMethod = requestMethod;
    }

    public String getRequestParams() {
    	return requestParams;
    }

    public void setRequestParams(String requestParams) {
    	this.requestParams = requestParams;
    }

    public String getRequestResult() {
    	return requestResult;
    }

    public void setRequestResult(String requestResult) {
    	this.requestResult = requestResult;
    }

    public String getClientIp() {
    	return clientIp;
    }

    public void setClientIp(String clientIp) {
    	this.clientIp = clientIp;
    }

    public Integer getStatus() {
    	return status;
    }

    public void setStatus(Integer status) {
    	this.status = status;
    }

    public Date getCreateTime() {
    	return createTime;
    }

    public void setCreateTime(Date createTime) {
    	this.createTime = createTime;
    }


    @Override
    public String toString() {
	    return "OperationLog{" +
	            ", id=" + id +
	            ", title=" + title +
	            ", businessType=" + businessType +
	            ", userType=" + userType +
	            ", userId=" + userId +
	            ", userName=" + userName +
	            ", phone=" + phone +
	            ", requestUrl=" + requestUrl +
	            ", requestMethod=" + requestMethod +
	            ", requestParams=" + requestParams +
	            ", requestResult=" + requestResult +
	            ", clientIp=" + clientIp +
	            ", status=" + status +
	            ", createTime=" + createTime +
	    "}";
    }

}
