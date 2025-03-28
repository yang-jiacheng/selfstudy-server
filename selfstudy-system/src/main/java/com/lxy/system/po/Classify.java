package com.lxy.system.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * Description: 图书馆
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

public class Classify implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //名称
    private String name;

    //描述
    private String description;

    //头像地址
    private String iconPath;

    //封面地址
    private String coverPath;

    //容纳人数（冗余字段）
    private Integer personCount;

    //是否开放使用 是否开放 (0否 1是)
    private Integer useType;

    //排序
    private Integer sort;

    //创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date createTime;

    //修改时间
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

    public String getIconPath() {
    	return iconPath;
    }

    public void setIconPath(String iconPath) {
    	this.iconPath = iconPath;
    }

    public String getCoverPath() {
    	return coverPath;
    }

    public void setCoverPath(String coverPath) {
    	this.coverPath = coverPath;
    }

    public Integer getPersonCount() {
    	return personCount;
    }

    public void setPersonCount(Integer personCount) {
    	this.personCount = personCount;
    }

    public Integer getUseType() {
    	return useType;
    }

    public void setUseType(Integer useType) {
    	this.useType = useType;
    }

    public Integer getSort() {
    	return sort;
    }

    public void setSort(Integer sort) {
    	this.sort = sort;
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
	    return "Classify{" +
	            ", id=" + id +
	            ", name=" + name +
	            ", description=" + description +
	            ", iconPath=" + iconPath +
	            ", coverPath=" + coverPath +
	            ", personCount=" + personCount +
	            ", useType=" + useType +
	            ", sort=" + sort +
	            ", createTime=" + createTime +
	            ", updateTime=" + updateTime +
	    "}";
    }

}
