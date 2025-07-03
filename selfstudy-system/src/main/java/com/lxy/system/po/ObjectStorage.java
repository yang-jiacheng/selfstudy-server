package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * Description: 对象存储
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

@Data
@TableName(value = "object_storage", autoResultMap = true)
public class ObjectStorage implements Serializable {

	@Serial
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

}
