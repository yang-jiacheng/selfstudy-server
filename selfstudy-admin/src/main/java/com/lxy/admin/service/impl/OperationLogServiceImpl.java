package com.lxy.admin.service.impl;

import com.lxy.admin.po.OperationLog;
import com.lxy.admin.mapper.OperationLogMapper;
import com.lxy.admin.service.OperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2025-03-21
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public void saveOperationLog(OperationLog operationLog) {

    }

}
