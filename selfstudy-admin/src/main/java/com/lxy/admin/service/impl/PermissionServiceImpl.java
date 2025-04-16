package com.lxy.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.admin.po.Permission;
import com.lxy.admin.mapper.PermissionMapper;
import com.lxy.admin.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.domain.R;
import com.lxy.system.vo.PermissionTreeVO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    @Override
    public R<Object> saveOrUpdatePermission(Permission permission) {

        return null;
    }

    @Override
    public List<PermissionTreeVO> getPermissionTree() {
        List<PermissionTreeVO> list =permissionMapper.getPermissionTree();
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
        List<PermissionTreeVO> rootList = list.stream()
                .filter(t -> t.getLevel() == 1)
                .collect(Collectors.toList());

        // 处理 level != 1 的，这些 节parentId 是上一级目录的 id
        Map<Integer, List<PermissionTreeVO>> nonLevel1CatalogMap = list.stream()
                .filter(t -> t.getLevel() != 1)
                .collect(Collectors.groupingBy(PermissionTreeVO::getParentId));
        PermissionTreeVO.recursionFnTree(rootList,nonLevel1CatalogMap);
        return rootList;
    }

}
