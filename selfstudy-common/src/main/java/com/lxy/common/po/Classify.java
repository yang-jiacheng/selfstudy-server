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
* @since 2022-10-22
*/

@ApiModel(value ="图书馆")
public class Classify implements Serializable {

	private static final long serialVersionUID = 1L;


    private Integer id;
    

    private String name;
    

    private String description;
    

    private String iconPath;
    

    private String coverPath;
    

    private Integer personCount;
    

    private Integer useType;
    

    private Integer sort;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date createTime;
    

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