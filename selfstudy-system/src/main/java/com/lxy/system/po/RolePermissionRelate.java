package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色权限关联表
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@NoArgsConstructor
@TableName(value = "role_permission_relate", autoResultMap = true)
public class RolePermissionRelate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 角色id
    private Long roleId;

    // 权限id
    private Long permissionId;
}
