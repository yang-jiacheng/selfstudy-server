package com.lxy.system.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.model.UiMeta;
import com.lxy.common.vo.base.BaseTreeVO;
import com.lxy.system.handler.MysqlJsonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 菜单权限树形结构VO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionTreeVO extends BaseTreeVO<PermissionTreeVO> {

    @Serial
    private static final long serialVersionUID = -3269124634376924194L;

    //权限类型：1目录 2菜单 3按钮
    private Integer type;

    // 权限名称
    private String title;

    // 权限信息
    private String permissionStr;

    /**
     * 前端路由元数据
     */
    @TableField(typeHandler = MysqlJsonTypeHandler.class)
    private UiMeta uiMeta;

    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

}
