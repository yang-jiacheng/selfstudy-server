package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色关联表
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@TableName(value = "admin_role_relate", autoResultMap = true)
public class AdminRoleRelate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 管理员id
    private Long adminId;

    // 角色id
    private Long roleId;

}
