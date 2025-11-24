package com.lxy.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.domain.R;
import com.lxy.system.mapper.PermissionMapper;
import com.lxy.system.po.Permission;
import com.lxy.system.service.PermissionService;
import com.lxy.system.vo.PermissionTreeVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getRolePermission(Long roleId) {
        return permissionMapper.getRolePermission(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<Object> saveOrUpdatePermission(Permission permission) {
        Date now = new Date();
        if (permission.getId() == null) {
            // 新增
            permission.setCreateTime(now);
            if (permission.getLevel() == 1) {
                permission.setParentId(-1L);
            }
            this.save(permission);
            // 维护节点的 nodePath
            updateNodePathInfo(permission);
            this.updateById(permission);
        } else {
            // 修改
            permission.setUpdateTime(now);
            this.updateById(permission);
        }
        return R.ok();
    }

    /**
     * 维护节点的 nodePath, namePath, idPath
     *
     * @author jiacheng yang.
     * @since 2025/4/20 1:31
     */
    private void updateNodePathInfo(Permission permission) {
        List<Long> nodePath = new ArrayList<>();
        nodePath.add(permission.getId());

        StringBuilder namePath = new StringBuilder(permission.getTitle());
        StringBuilder idPath = new StringBuilder(permission.getId().toString());

        if (permission.getParentId() != -1) {
            Permission parentNode = this.getById(permission.getParentId());
            if (parentNode != null) {
                List<Long> parentNodePathList = parentNode.getNodePath();
                if (parentNodePathList != null) {
                    nodePath = new ArrayList<>(parentNodePathList); // 避免污染原数据
                    nodePath.add(permission.getId());
                }

                String parentNamePath = StrUtil.removeSuffix(parentNode.getNamePath(), "/");
                String parentIdPath = StrUtil.removeSuffix(parentNode.getIdPath(), "/");

                namePath.insert(0, parentNamePath + "/");
                idPath.insert(0, parentIdPath + "/");
            }
        }

        if (!StrUtil.endWith(namePath, "/")) {
            namePath.append("/");
        }
        if (!StrUtil.endWith(idPath, "/")) {
            idPath.append("/");
        }

        permission.setNodePath(nodePath);
        permission.setNamePath(namePath.toString());
        permission.setIdPath(idPath.toString());
    }

    @Override
    public List<PermissionTreeVO> getPermissionTree() {
        List<PermissionTreeVO> list = permissionMapper.getPermissionTree();
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
        list = PermissionTreeVO.buildTree(list);
        return list;
    }

    @Override
    public List<Permission> getPermissionListAndChildren(Collection<Long> ids) {
        return permissionMapper.getPermissionListAndChildren(ids);
    }

    @Override
    public List<Permission> getPermissionListAndParent(Collection<Long> ids) {
        return permissionMapper.getPermissionListAndParent(ids);
    }

    @Override
    public void removePermission(Long id) {
        List<Permission> list = this.getPermissionListAndChildren(Collections.singletonList(id));
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<Long> ids = list.stream().map(Permission::getId).toList();
        this.removeByIds(ids);
    }

    @Override
    public List<PermissionTreeVO> getMinePermissionTree(Long userId) {
        List<PermissionTreeVO> list = permissionMapper.getMinePermissionTree(userId);
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
        list = PermissionTreeVO.buildTree(list);
        return list;
    }

}
