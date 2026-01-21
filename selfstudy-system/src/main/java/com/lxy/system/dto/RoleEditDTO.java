package com.lxy.system.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色修改DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
public class RoleEditDTO {

    private Long id;

    // 角色名称
    @NotNull(message = "角色名称不能为空")
    private String name;

    // 角色描述
    private String description;

    // 角色权限
    @NotEmpty(message = "菜单权限不能为空")
    private List<Long> permissionIds;

}
