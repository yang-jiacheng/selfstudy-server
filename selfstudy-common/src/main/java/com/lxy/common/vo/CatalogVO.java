package com.lxy.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 自习室vo
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/24 11:12
 */

@Data
public class CatalogVO implements Serializable {
    private static final long serialVersionUID = 162687177993801603L;

    /**
     * 自习室id
     */
    private Long catalogId;

    /**
     * 图书馆id
     */
    private Long classifyId;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 层级 1楼层 2自习室
     */
    private Integer level;

    /**
     * 昵称
     */
    private String catalogName;

    /**
     * 容纳人数
     */
    private Integer personCount;

    /**
     * 当前人数
     */
    private Integer currCount;

    /**
     * 排序
     */
    private Integer sort;

}
