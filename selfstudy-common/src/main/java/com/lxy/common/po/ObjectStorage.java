package com.lxy.common.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
* @author jiacheng yang.
* @since 2024-12-14
*/

@ApiModel(value ="对象存储")
public class ObjectStorage implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
   	@ApiModelProperty(value = "原始文件名")
    private String fileName;

    
   	@ApiModelProperty(value = "下载地址")
    private String downloadUrl;
    
   	@ApiModelProperty(value = "创建人id")
    private Integer creatorId;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
   	@ApiModelProperty(value = "创建时间")
    private Date createTime;
    
   	@ApiModelProperty(value = "文件大小 MB")
    private BigDecimal fileSize;
    
    public Integer getId() {
    	return id;
    }
    
    public void setId(Integer id) {
    	this.id = id;
    }

    public String getFileName() {
    	return fileName;
    }
    
    public void setFileName(String fileName) {
    	this.fileName = fileName;
    }


    public String getDownloadUrl() {
    	return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
    	this.downloadUrl = downloadUrl;
    }

    public Integer getCreatorId() {
    	return creatorId;
    }
    
    public void setCreatorId(Integer creatorId) {
    	this.creatorId = creatorId;
    }

    public Date getCreateTime() {
    	return createTime;
    }
    
    public void setCreateTime(Date createTime) {
    	this.createTime = createTime;
    }

    public BigDecimal getFileSize() {
    	return fileSize;
    }
    
    public void setFileSize(BigDecimal fileSize) {
    	this.fileSize = fileSize;
    }


    @Override
    public String toString() {
	    return "ObjectStorage{" +
	            ", id=" + id +
	            ", fileName=" + fileName +
	            ", downloadUrl=" + downloadUrl +
	            ", creatorId=" + creatorId +
	            ", createTime=" + createTime +
	            ", fileSize=" + fileSize +
	    "}";
    }

}