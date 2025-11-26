package com.lxy.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 图书馆详情 vo
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/24 11:08
 */

@Data
public class ClassifyDetailVO implements Serializable {

    private static final long serialVersionUID = 7065295037862538696L;

    /**
     * 图书馆id
     */
    private Long id;

    /**
     * 图书馆昵称
     */
    private String name;

    /**
     * 图书馆描述
     */
    private String description;

    /**
     * 头像地址
     */
    private String iconPath;

    /**
     * 封面地址
     */
    private String coverPath;

    /**
     * 自习室
     */
    private List<CatalogVO> rooms;

}
