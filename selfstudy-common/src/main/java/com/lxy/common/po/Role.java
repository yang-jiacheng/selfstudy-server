package com.lxy.common.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * Description: 角色表
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

    //角色id
	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //角色名称
    private String name;

    //角色描述
    private String description;

    //每条记录的创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date createTime;

    //每条记录的更新时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
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
