package com.lxy.common.vo.base;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础树形结构VO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2026/1/20 17:37
 */

@Data
public abstract class BaseTreeVO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2975022275511378192L;

    private String id;

    private String parentId;

    private List<T> children = new ArrayList<>();

}
