package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.util.DateCusUtil;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 版本控制
 *
 * @author jiacheng yang.
 * @since 2025-04-03
 */

@Data
@TableName(value = "version", autoResultMap = true)
public class Version implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    // 版本号
    private Integer versionCode;

    // 版本名称
    private String versionName;

    // 下载地址
    private String downloadUrl;

    // 是否强制更新 1是 0 否
    private Integer status;

    // 是否检测更新 1是 0 否
    private Integer checkUpdate;

    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date updateTime;

}
