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

@ApiModel(value ="角色表")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

   	@ApiModelProperty(value = "角色id")
	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
   	@ApiModelProperty(value = "角色名称")
    private String name;
    
   	@ApiModelProperty(value = "角色描述")
    private String description;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
   	@ApiModelProperty(value = "每条记录的创建时间")
    private Date createTime;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
   	@ApiModelProperty(value = "每条记录的更新时间")
    private Date updateTime;
    
    public Integer getId() {
    	return id;
    }
    
    public void setId(Integer id) {
    	this.id = id;
    }

    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }

    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }

    public Date getCreateTime() {
    	return createTime;
    }
    
    public void setCreateTime(Date createTime) {
    	this.createTime = createTime;
    }

    public Date getUpdateTime() {
    	return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
    	this.updateTime = updateTime;
    }


    @Override
    public String toString() {
	    return "Role{" +
	            ", id=" + id +
	            ", name=" + name +
	            ", description=" + description +
	            ", createTime=" + createTime +
	            ", updateTime=" + updateTime +
	    "}";
    }

}