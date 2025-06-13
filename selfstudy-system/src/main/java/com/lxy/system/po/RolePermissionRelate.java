package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * Description: 角色权限关联表
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

public class RolePermissionRelate implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //角色id
    private Integer roleId;

    //权限id
    private Integer permissionId;

    public Integer getId() {
    	return id;
    }

    public void setId(Integer id) {
    	this.id = id;
    }

    public Integer getRoleId() {
    	return roleId;
    }

    public void setRoleId(Integer roleId) {
    	this.roleId = roleId;
    }

    public Integer getPermissionId() {
    	return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
    	this.permissionId = permissionId;
    }


    @Override
    public String toString() {
	    return "RolePermissionRelate{" +
	            ", id=" + id +
	            ", roleId=" + roleId +
	            ", permissionId=" + permissionId +
	    "}";
    }

}
