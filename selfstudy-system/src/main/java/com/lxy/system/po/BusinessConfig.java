package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@TableName(value = "business_config", autoResultMap = true)
public class BusinessConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String bkey;

    private String bvalue;

    // 描述
    private String description;

    // 0 后台不可编辑 1可编辑
    private Integer showStatus;

}
