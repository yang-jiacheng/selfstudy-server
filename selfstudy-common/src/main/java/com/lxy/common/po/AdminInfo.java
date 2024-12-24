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
* @since 2022-10-08
*/

@ApiModel(value ="")
public class AdminInfo implements Serializable {

	private static final long serialVersionUID = 1L;

   	@ApiModelProperty(value = "管理员自增主键")
	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    private String phone;
    
   	@ApiModelProperty(value = "用户名")
    private String username;
    
   	@ApiModelProperty(value = "昵称")
    private String name;
    
   	@ApiModelProperty(value = "密码")
    private String password;
    
   	@ApiModelProperty(value = "头像地址")
    private String profilePath;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
   	@ApiModelProperty(value = "修改时间")
    private Date updateTime;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
   	@ApiModelProperty(value = "创建时间")
    private Date createTime;
    
   	@ApiModelProperty(value = "状态 1正常 2禁用")
    private Integer status;
    
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

    public String getUsername() {
    	return username;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }

    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }

    public String getPassword() {
    	return password;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }

    public String getProfilePath() {
    	return profilePath;
    }
    
    public void setProfilePath(String profilePath) {
    	this.profilePath = profilePath;
    }

    public Date getUpdateTime() {
    	return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
    	this.updateTime = updateTime;
    }

    public Date getCreateTime() {
    	return createTime;
    }
    
    public void setCreateTime(Date createTime) {
    	this.createTime = createTime;
    }

    public Integer getStatus() {
    	return status;
    }
    
    public void setStatus(Integer status) {
    	this.status = status;
    }


    @Override
    public String toString() {
	    return "AdminInfo{" +
	            ", id=" + id +
	            ", phone=" + phone +
	            ", username=" + username +
	            ", name=" + name +
	            ", password=" + password +
	            ", profilePath=" + profilePath +
	            ", updateTime=" + updateTime +
	            ", createTime=" + createTime +
	            ", status=" + status +
	    "}";
    }

}