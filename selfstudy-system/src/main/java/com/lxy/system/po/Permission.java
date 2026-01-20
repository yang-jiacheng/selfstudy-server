package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.model.UiMeta;
import com.lxy.system.handler.ListLongTypeHandler;
import com.lxy.system.handler.MysqlJsonTypeHandler;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 权限表
 *
 * @author jiacheng yang.
 * @since 2025-04-16
 */

@Data
@TableName(value = "permission", autoResultMap = true)
public class Permission implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // 主键id
    private Long id;

    // 父id
    private Long parentId;

    @TableField(typeHandler = ListLongTypeHandler.class)
    private List<Long> nodePath;

    private String namePath;

    private String idPath;

    //权限类型：1目录 2菜单 3按钮
    private Integer type;

    // 权限名称
    private String title;

    // 权限信息
    private String permissionStr;

    @TableField(typeHandler = MysqlJsonTypeHandler.class)
    private UiMeta uiMeta;

    // 每条记录的创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    // 每条记录的更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    private Integer sort;


}
