package com.lxy.common.vo;

import lombok.Data;

/**
 * 系统字典VO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2026/2/1 23:14
 */

@Data
public class DictVO {

    private Long id;

    /**
     * 字典值
     */
    private String value;

    /**
     * 字典名称
     */
    private String label;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;

}
