package com.lxy.admin.mapper;

import com.lxy.admin.po.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.system.vo.PermissionTreeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> getRolePermission(Integer roleId);

    List<PermissionTreeVO> getPermissionTree();
}
