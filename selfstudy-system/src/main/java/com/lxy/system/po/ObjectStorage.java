package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.util.DateCusUtil;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 对象存储
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@TableName(value = "object_storage", autoResultMap = true)
public class ObjectStorage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date createTime;

}
