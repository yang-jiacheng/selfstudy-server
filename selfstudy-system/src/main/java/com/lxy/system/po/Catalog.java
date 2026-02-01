package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.util.DateCusUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 自习室
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@TableName(value = "catalog", autoResultMap = true)
public class Catalog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 图书馆id
    private Long classifyId;

    // 父id
    private Long parentId;

    // 层级
    private Integer level;

    // 节点名称
    private String name;

    // 容纳人数
    private Integer personCount;

    // 排序
    private Integer sort;

    // 创建时间
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date createTime;

    // 修改时间
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date updateTime;

}
