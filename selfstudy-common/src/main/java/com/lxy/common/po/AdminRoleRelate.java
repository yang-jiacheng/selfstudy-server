package com.lxy.common.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * Description: 用户角色关联表
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

public class AdminRoleRelate implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //管理员id
    private Integer adminId;

    //角色id
    private Integer roleId;

    public Integer getId() {
    	return id;
    }

    public void setId(Integer id) {
    	this.id = id;
    }

    public Integer getAdminId() {
    	return adminId;
    }

    public void setAdminId(Integer adminId) {
    	this.adminId = adminId;
    }

    public Integer getRoleId() {
    	return roleId;
    }

    public void setRoleId(Integer roleId) {
    	this.roleId = roleId;
    }


    @Override
    public String toString() {
	    return "AdminRoleRelate{" +
	            ", id=" + id +
	            ", adminId=" + adminId +
	            ", roleId=" + roleId +
	    "}";
    }

}
