package com.lxy.system.dto;


import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2024/12/14 16:19
 * @Version: 1.0
 */
public class ObjectStorageDTO implements Serializable {
    private static final long serialVersionUID = -6584352603005168007L;

    @NotNull(message = "文件名不能为空")
    private String fileName;

    @NotNull(message = "文件路径不能为空")
    private String downloadUrl;

    @NotNull(message = "文件大小不能为空")
    private BigDecimal fileSize;

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

    public BigDecimal getFileSize() {
        return fileSize;
    }

    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }
}
