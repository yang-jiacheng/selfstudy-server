package com.lxy.common.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * Description: 权限表
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

    //权限id
	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //权限名称
    private String name;

    //权限描述
    private String description;

    //spring security权限信息
    private String url;

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

    public String getUrl() {
    	return url;
    }

    public void setUrl(String url) {
    	this.url = url;
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
	    return "Permission{" +
	            ", id=" + id +
	            ", name=" + name +
	            ", description=" + description +
	            ", url=" + url +
	            ", createTime=" + createTime +
	            ", updateTime=" + updateTime +
	    "}";
    }

}
