package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.util.DateCusUtil;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 *
 * @author jiacheng yang.
 * @since 2025-03-21
 */

@Data
@TableName(value = "operation_log", autoResultMap = true)
public class OperationLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型：字典`log_business_type`
     */
    private Integer businessType;

    /**
     * 用户类型：字典`log_user_type`
     */
    private Integer userType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求结果
     */
    private String requestResult;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 操作状态：字典`log_status`
     */
    private String status;

    /**
     * 请求时间
     */
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date createTime;

    private String durationStr;

    public OperationLog() {}

    public OperationLog(String title, Integer businessType, Integer userType, Long userId, String requestUrl,
        String requestMethod, String requestParams, String requestResult, String clientIp, String status,
        String durationStr) {
        this.title = title;
        this.businessType = businessType;
        this.userType = userType;
        this.userId = userId;
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.requestParams = requestParams;
        this.requestResult = requestResult;
        this.clientIp = clientIp;
        this.status = status;
        this.durationStr = durationStr;
    }
}
