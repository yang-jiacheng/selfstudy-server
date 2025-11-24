package com.lxy.system.vo;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单权限树形结构VO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
public class PermissionTreeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3269124634376924194L;

    private Long id;

    // 父id
    private Long parentId;

    private Integer level;

    // 权限名称
    private String title;

    // 权限信息
    private String permissionStr;

    // 路由名称
    private String name;

    // 路由路径
    private String path;

    // 路由组件
    private String component;

    private String icon;

    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    private List<PermissionTreeVO> children;

    /**
     * 递归处理树形结构
     *
     * @param list 树形结构列表
     * @param map 父子关系映射
     * @author jiacheng yang.
     * @since 2025/4/16 18:56
     */
    public static void recursionFnTree(List<PermissionTreeVO> list, Map<Integer, List<PermissionTreeVO>> map) {
        for (PermissionTreeVO treeSelect : list) {
            List<PermissionTreeVO> childList = map.get(treeSelect.getId());
            treeSelect.setChildren(childList);
            if (CollUtil.isNotEmpty(childList)) {
                recursionFnTree(childList, map);
            }
        }
    }

    public static List<PermissionTreeVO> buildTree(List<PermissionTreeVO> flatList) {
        List<PermissionTreeVO> roots = new ArrayList<>();
        if (CollUtil.isEmpty(flatList)) {
            return roots;
        }
        Map<Long, PermissionTreeVO> nodeMap = new HashMap<>(flatList.size());
        for (PermissionTreeVO node : flatList) {
            node.setChildren(new ArrayList<>());
            nodeMap.put(node.getId(), node);
        }
        for (PermissionTreeVO node : flatList) {
            Long parentId = node.getParentId() == null ? 0 : node.getParentId();
            PermissionTreeVO parentNode = nodeMap.get(parentId);
            if (parentNode == null) {
                // 作为根节点
                roots.add(node);
            } else {
                parentNode.getChildren().add(node);
            }
        }
        return roots;
    }

}
