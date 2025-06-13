package com.lxy.system.mapper;

import com.lxy.system.po.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.system.vo.PermissionTreeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
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

    List<Permission> getPermissionListAndChildren(@Param("ids") Collection<Integer> ids);

    List<Permission> getPermissionListAndParent(@Param("ids") Collection<Integer> ids);
}
