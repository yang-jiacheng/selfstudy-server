package com.lxy.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.lxy.system.po.Permission;
import com.lxy.system.mapper.PermissionMapper;
import com.lxy.system.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.domain.R;
import com.lxy.system.vo.PermissionTreeVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private  PermissionMapper permissionMapper;

    @Override
    public List<Permission> getRolePermission(Integer roleId) {
        return permissionMapper.getRolePermission(roleId);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<Object> saveOrUpdatePermission(Permission permission) {
        Date now = new Date();
        if (permission.getId() == null) {
            // 新增
            permission.setCreateTime(now);
            if (permission.getLevel() == 1){
                permission.setParentId(-1);
            }
            this.save(permission);
            //维护节点的 nodePath
            updateNodePathInfo(permission);
            this.updateById(permission);
        }else {
            // 修改
            permission.setUpdateTime(now);
            this.updateById(permission);
        }
        return R.ok();
    }

    /**
     * 维护节点的 nodePath, namePath, idPath
     * @author jiacheng yang.
     * @since 2025/4/20 1:31
     */
    private void updateNodePathInfo(Permission permission) {
        List<Integer> nodePath = new ArrayList<>();
        nodePath.add(permission.getId());
        StringBuilder namePath = new StringBuilder(permission.getTitle());
        StringBuilder idPath = new StringBuilder(permission.getId().toString());



        if (permission.getParentId() != -1) {
            Permission parentNode = this.getById(permission.getParentId());
            if (parentNode != null) {
                List<Integer> parentNodePathList = parentNode.getNodePath();
                if (parentNodePathList != null) {
                    parentNodePathList.add(permission.getId());
                    nodePath = parentNodePathList;
                }
                namePath.insert(0, parentNode.getNamePath() + "/");
                idPath.insert(0, parentNode.getIdPath() + "/");
            }
        }

        if (!namePath.toString().endsWith("/")) {
            namePath.append("/");
        }
        if (!idPath.toString().endsWith("/")) {
            idPath.append("/");
        }

        permission.setNodePath(nodePath);
        permission.setNamePath(namePath.toString());
        permission.setIdPath(idPath.toString());
    }

    @Override
    public List<PermissionTreeVO> getPermissionTree() {
        List<PermissionTreeVO> list =permissionMapper.getPermissionTree();
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
//        List<PermissionTreeVO> rootList = list.stream()
//                .filter(t -> t.getLevel() == 1)
//                .collect(Collectors.toList());
//
//        // 处理 level != 1 的，这些 节parentId 是上一级目录的 id
//        Map<Integer, List<PermissionTreeVO>> nonLevel1CatalogMap = list.stream()
//                .filter(t -> t.getLevel() != 1)
//                .collect(Collectors.groupingBy(PermissionTreeVO::getParentId));
//        PermissionTreeVO.recursionFnTree(rootList,nonLevel1CatalogMap);
        list =  PermissionTreeVO.buildTree(list);
        return list;
    }

    @Override
    public List<Permission> getPermissionListAndChildren(Collection<Integer> ids) {
        return permissionMapper.getPermissionListAndChildren(ids);
    }

    @Override
    public List<Permission> getPermissionListAndParent(Collection<Integer> ids) {
        return permissionMapper.getPermissionListAndParent(ids);
    }

    @Override
    public void removePermission(Integer id) {
        List<Permission> list = this.getPermissionListAndChildren(Arrays.asList(id));
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<Integer> ids = list.stream()
                .map(Permission::getId).toList();
        this.removeByIds(ids);
    }

}
