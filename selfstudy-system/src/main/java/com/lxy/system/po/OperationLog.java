package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 *
 * @author jiacheng yang.
 * @since 2025-03-21
 */

@Data
@TableName("operation_log")
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 模块标题
    private String title;

    // 业务类型（0其它 1新增 2修改 3删除 4导入 5导出）
    private Integer businessType;

    // 0后台用户 1APP用户
    private Integer userType;

    // 用户id
    private Long userId;

    // 请求地址
    private String requestUrl;

    // 请求方式
    private String requestMethod;

    // 请求参数
    private String requestParams;

    // 请求结果
    private String requestResult;

    // 客端ip
    private String clientIp;

    // 操作状态（0成功 1失败）
    private Integer status;

    // 请求时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    private String durationStr;

    public OperationLog() {}

    public OperationLog(String title, Integer businessType, Integer userType, Long userId, String requestUrl,
        String requestMethod, String requestParams, String requestResult, String clientIp, Integer status,
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
