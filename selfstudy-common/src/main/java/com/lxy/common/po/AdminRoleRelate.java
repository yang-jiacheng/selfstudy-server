package com.lxy.common.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
* @author jiacheng yang.
* @since 2022-10-08
*/

@ApiModel(value ="用户角色关联表")
public class AdminRoleRelate implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
   	@ApiModelProperty(value = "管理员id")
    private Integer adminId;
    
   	@ApiModelProperty(value = "角色id")
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