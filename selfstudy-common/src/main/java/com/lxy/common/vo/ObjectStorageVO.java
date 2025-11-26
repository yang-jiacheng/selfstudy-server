package com.lxy.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 对象存储VO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2024/12/14 15:47
 */

@Data
public class ObjectStorageVO implements Serializable {
    private static final long serialVersionUID = 4177033914831871822L;
    private Long id;

    // 原始文件名
    private String fileName;

    // 文件大小 MB
    private BigDecimal fileSize;

    // 下载地址
    private String downloadUrl;

    // 创建人id
    private Long creatorId;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    private String creatorName;

}
