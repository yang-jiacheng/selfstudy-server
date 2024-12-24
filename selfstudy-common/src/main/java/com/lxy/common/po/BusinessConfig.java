package com.lxy.common.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
* @author jiacheng yang.
* @since 2022-10-08
*/

@ApiModel(value ="业务配置表")
public class BusinessConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    private String bkey;
    
    private String bvalue;
    
   	@ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "0 后台不可编辑 1可编辑")
    private Integer showStatus;
    
    public Integer getId() {
    	return id;
    }
    
    public void setId(Integer id) {
    	this.id = id;
    }

    public String getBkey() {
    	return bkey;
    }
    
    public void setBkey(String bkey) {
    	this.bkey = bkey;
    }

    public String getBvalue() {
    	return bvalue;
    }
    
    public void setBvalue(String bvalue) {
    	this.bvalue = bvalue;
    }

    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }

    public Integer getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    @Override
    public String toString() {
        return "BusinessConfig{" +
                "id=" + id +
                ", bkey='" + bkey + '\'' +
                ", bvalue='" + bvalue + '\'' +
                ", description='" + description + '\'' +
                ", showStatus=" + showStatus +
                '}';
    }
}