package com.lxy.admin.service;

import com.lxy.admin.po.OperationLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 操作日志 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2025-03-21
 */
public interface OperationLogService extends IService<OperationLog> {

    void saveOperationLog(OperationLog operationLog);

}
