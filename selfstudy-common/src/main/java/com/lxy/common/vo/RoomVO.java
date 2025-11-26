package com.lxy.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 自习室vo
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2023/01/13 10:37
 */

@Data
public class RoomVO implements Serializable {
    private static final long serialVersionUID = -9081562248724816519L;

    private Long id;
    private Long parentId;
    private String name;
    private Integer personCount;
    private String parentName;
    private String libraryName;

}
