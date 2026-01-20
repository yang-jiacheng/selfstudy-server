package com.lxy.common.model;

import lombok.Data;

/**
 * 前端元数据
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2026/1/20 17:27
 */

@Data
public class UiMeta {

    // 路由名称
    private String name;

    // 路由路径
    private String path;

    // 路由组件
    private String component;

    // 图标 例如 Element-plus 的 Plus,直接写Plus即可
    private String icon;

}
