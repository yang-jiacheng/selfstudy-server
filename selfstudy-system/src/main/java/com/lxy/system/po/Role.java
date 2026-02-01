package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.util.DateCusUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色表
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@NoArgsConstructor
@TableName(value = "role", autoResultMap = true)
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    // 角色id

    private Long id;

    // 角色名称
    private String name;

    // 角色描述
    private String description;

    // 每条记录的创建时间
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date createTime;

    // 每条记录的更新时间
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date updateTime;

}
