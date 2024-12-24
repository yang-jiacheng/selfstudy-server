package com.lxy.common.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
* @author jiacheng yang.
* @since 2022-12-19
*/

@ApiModel(value ="")
public class PhoneCode implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    private String phone;
    
    private String code;

    private Integer useStatus;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date createTime;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
   	@ApiModelProperty(value = "过期时间")
    private Date endTime;

    public PhoneCode() {
    }

    public PhoneCode(String phone, String code,Integer useStatus, Date createTime, Date endTime) {
        this.phone = phone;
        this.code = code;
        this.useStatus = useStatus;
        this.createTime = createTime;
        this.endTime = endTime;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public Integer getId() {
    	return id;
    }
    
    public void setId(Integer id) {
    	this.id = id;
    }

    public String getPhone() {
    	return phone;
    }
    
    public void setPhone(String phone) {
    	this.phone = phone;
    }

    public String getCode() {
    	return code;
    }
    
    public void setCode(String code) {
    	this.code = code;
    }

    public Date getCreateTime() {
    	return createTime;
    }
    
    public void setCreateTime(Date createTime) {
    	this.createTime = createTime;
    }

    public Date getEndTime() {
    	return endTime;
    }
    
    public void setEndTime(Date endTime) {
    	this.endTime = endTime;
    }


    @Override
    public String toString() {
	    return "PhoneCode{" +
	            ", id=" + id +
	            ", phone=" + phone +
	            ", code=" + code +
	            ", createTime=" + createTime +
	            ", endTime=" + endTime +
	    "}";
    }

}