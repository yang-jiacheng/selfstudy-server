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

@ApiModel(value ="节点")
public class Catalog implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
   	@ApiModelProperty(value = "图书馆id")
    private Integer classifyId;
    
   	@ApiModelProperty(value = "父id")
    private Integer parentId;
    
   	@ApiModelProperty(value = "层级")
    private Integer level;
    
   	@ApiModelProperty(value = "节点名称")
    private String name;
    
   	@ApiModelProperty(value = "容纳人数")
    private Integer personCount;
    
   	@ApiModelProperty(value = "排序")
    private Integer sort;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
   	@ApiModelProperty(value = "创建时间")
    private Date createTime;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
   	@ApiModelProperty(value = "修改时间")
    private Date updateTime;
    
    public Integer getId() {
    	return id;
    }
    
    public void setId(Integer id) {
    	this.id = id;
    }

    public Integer getClassifyId() {
    	return classifyId;
    }
    
    public void setClassifyId(Integer classifyId) {
    	this.classifyId = classifyId;
    }

    public Integer getParentId() {
    	return parentId;
    }
    
    public void setParentId(Integer parentId) {
    	this.parentId = parentId;
    }

    public Integer getLevel() {
    	return level;
    }
    
    public void setLevel(Integer level) {
    	this.level = level;
    }

    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }

    public Integer getPersonCount() {
    	return personCount;
    }
    
    public void setPersonCount(Integer personCount) {
    	this.personCount = personCount;
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
	    return "Catalog{" +
	            ", id=" + id +
	            ", classifyId=" + classifyId +
	            ", parentId=" + parentId +
	            ", level=" + level +
	            ", name=" + name +
	            ", personCount=" + personCount +
	            ", sort=" + sort +
	            ", createTime=" + createTime +
	            ", updateTime=" + updateTime +
	    "}";
    }

}