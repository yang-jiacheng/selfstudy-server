package com.lxy.admin.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import java.io.Serial;
import java.util.List;

/**
 * 权限表
 * @author jiacheng yang.
 * @since 2025-04-16
 */

@Data
@TableName(value = "permission", autoResultMap = true)
public class Permission implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

    //权限id
	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //父id
    private Integer parentId;

    private Integer level;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> nodePath;

    private String namePath;

    private String idPath;

    //权限名称
    private String title;

    //权限信息
    private String permissionStr;

    //路由名称
    private String name;

    //路由路径
    private String path;

    //路由组件
    private String component;

    //图标  例如：<el-icon ><i-ep-house /></el-icon>填写：house
    private String icon;

    //每条记录的创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date createTime;

    //每条记录的更新时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date updateTime;

    private Integer sort;



}
