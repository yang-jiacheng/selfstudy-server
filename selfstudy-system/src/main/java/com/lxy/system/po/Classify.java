package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 图书馆
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@TableName(value = "classify", autoResultMap = true)
public class Classify implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 名称
    private String name;

    // 描述
    private String description;

    // 头像地址
    private String iconPath;

    // 封面地址
    private String coverPath;

    // 容纳人数（冗余字段）
    private Integer personCount;

    // 是否开放使用 是否开放 (0否 1是)
    private Integer useType;

    // 排序
    private Integer sort;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    // 修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

}
