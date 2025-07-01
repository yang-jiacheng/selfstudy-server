package com.lxy.system.vo;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
public class CatalogTreeVO implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -6714104747783083093L;

    private String id;

    private String parentId;

    private Integer level;

    private String name;

    private Integer sort;

    private List<CatalogTreeVO> children;

    public static List<CatalogTreeVO> buildTree(List<CatalogTreeVO> flatList) {
        List<CatalogTreeVO> roots = new ArrayList<>();
        if (CollUtil.isEmpty(flatList)) {
            return roots;
        }
        Map<String, CatalogTreeVO> nodeMap = new HashMap<>(flatList.size());
        for (CatalogTreeVO node : flatList) {
            node.setChildren(new ArrayList<>());
            nodeMap.put(node.getId(), node);
        }
        for (CatalogTreeVO node : flatList) {
            String parentId = node.getParentId() == null ? "-1" : node.getParentId();
            CatalogTreeVO parentNode = nodeMap.get(parentId);
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
