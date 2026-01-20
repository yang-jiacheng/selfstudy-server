package com.lxy.common.util;

import cn.hutool.core.collection.CollUtil;
import com.lxy.common.vo.base.BaseTreeVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形结构工具类
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2026/1/20 17:35
 */

public class TreeUtil {

    public static <T extends BaseTreeVO<T>> List<T> buildTree(List<T> list) {
        List<T> roots = new ArrayList<>();
        if (CollUtil.isEmpty(list)) {
            return roots;
        }
        Map<Long, T> nodeMap = new HashMap<>(list.size());
        for (T node : list) {
            node.setChildren(new ArrayList<>());
            nodeMap.put(node.getId(), node);
        }
        for (T node : list) {
            Long parentId = node.getParentId();
            T parent = nodeMap.get(parentId);
            if (parent == null) {
                roots.add(node);
            } else {
                parent.getChildren().add(node);
            }
        }
        return roots;
    }

}
