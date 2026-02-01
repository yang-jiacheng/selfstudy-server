package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.util.DateCusUtil;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统字典表
 *
 * @author jiacheng yang.
 * @since 2026-02-01
 */

@Data
@TableName(value = "sys_dict", autoResultMap = true)
public class SysDict implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 字典类型描述
     */
    private String dictDescription;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 关联字典类型
     */
    private String relationDictType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date updateTime;

}
