package com.lxy.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.system.mapper.OperationLogMapper;
import com.lxy.system.po.OperationLog;
import com.lxy.system.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2025-03-21
 */

@Slf4j
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

}
