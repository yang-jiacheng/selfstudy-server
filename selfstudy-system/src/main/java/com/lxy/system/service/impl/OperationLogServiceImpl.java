package com.lxy.system.service.impl;

import com.lxy.common.util.ThreadPoolUtil;
import com.lxy.system.event.OperationLogEvent;
import com.lxy.system.po.OperationLog;
import com.lxy.system.mapper.OperationLogMapper;
import com.lxy.system.service.OperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
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

    @EventListener
    @Override
    public void saveOperationLog(OperationLogEvent event) {
        OperationLog operationLog = event.getOperationLog();
        ThreadPoolUtil.execute(() -> {
            try{
                this.save(operationLog);
                log.info("异步保存操作日志成功");
            } catch (Exception e) {
                log.error("异步保存操作日志失败", e);
            }
        });
    }

}
