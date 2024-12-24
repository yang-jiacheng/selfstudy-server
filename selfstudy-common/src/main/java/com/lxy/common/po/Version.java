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
* @since 2023-03-10
*/

@ApiModel(value ="")
public class Version implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
   	@ApiModelProperty(value = "版本号")
    private Integer versionCode;
    
   	@ApiModelProperty(value = "版本名称")
    private String versionName;
    
   	@ApiModelProperty(value = "下载地址")
    private String downloadUrl;
    
   	@ApiModelProperty(value = "是否强制更新 1是 0 否")
    private Integer status;
    
   	@ApiModelProperty(value = "是否检测更新 1是 0 否")
    private Integer checkUpdate;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date updateTime;
    
    public Integer getId() {
    	return id;
    }
    
    public void setId(Integer id) {
    	this.id = id;
    }

    public Integer getVersionCode() {
    	return versionCode;
    }
    
    public void setVersionCode(Integer versionCode) {
    	this.versionCode = versionCode;
    }

    public String getVersionName() {
    	return versionName;
    }
    
    public void setVersionName(String versionName) {
    	this.versionName = versionName;
    }

    public String getDownloadUrl() {
    	return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
    	this.downloadUrl = downloadUrl;
    }

    public Integer getStatus() {
    	return status;
    }
    
    public void setStatus(Integer status) {
    	this.status = status;
    }

    public Integer getCheckUpdate() {
    	return checkUpdate;
    }
    
    public void setCheckUpdate(Integer checkUpdate) {
    	this.checkUpdate = checkUpdate;
    }

    public Date getUpdateTime() {
    	return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
    	this.updateTime = updateTime;
    }


    @Override
    public String toString() {
	    return "Version{" +
	            ", id=" + id +
	            ", versionCode=" + versionCode +
	            ", versionName=" + versionName +
	            ", downloadUrl=" + downloadUrl +
	            ", status=" + status +
	            ", checkUpdate=" + checkUpdate +
	            ", updateTime=" + updateTime +
	    "}";
    }

}