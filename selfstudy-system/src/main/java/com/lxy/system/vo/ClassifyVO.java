package com.lxy.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.util.DateCusUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 图书馆vo
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/19 17:59
 */

@Data
public class ClassifyVO implements Serializable {

    private static final long serialVersionUID = 807766301683388704L;

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
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date createTime;

    // 修改时间
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    // 学习人数
    private Integer studyCount;

}
