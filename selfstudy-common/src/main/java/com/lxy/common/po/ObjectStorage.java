package com.lxy.common.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * Description: 对象存储
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

public class ObjectStorage implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //原始文件名
    private String fileName;

    //文件大小 MB
    private BigDecimal fileSize;

    //下载地址
    private String downloadUrl;

    //创建人id
    private Integer creatorId;

    //创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date createTime;

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

    public BigDecimal getFileSize() {
    	return fileSize;
    }

    public void setFileSize(BigDecimal fileSize) {
    	this.fileSize = fileSize;
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


    @Override
    public String toString() {
	    return "ObjectStorage{" +
	            ", id=" + id +
	            ", fileName=" + fileName +
	            ", fileSize=" + fileSize +
	            ", downloadUrl=" + downloadUrl +
	            ", creatorId=" + creatorId +
	            ", createTime=" + createTime +
	    "}";
    }

}
