package com.lxy.framework.event.listener;

import com.lxy.common.util.ThreadPoolUtil;
import com.lxy.framework.event.OperationLogEvent;
import com.lxy.system.po.OperationLog;
import com.lxy.system.service.OperationLogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Slf4j
@Component
public class OperationLogEventListener {

    @Resource
    private OperationLogService operationLogService;

    @EventListener
    public void saveOperationLog(OperationLogEvent event) {
        OperationLog operationLog = event.getOperationLog();
        ThreadPoolUtil.execute(() -> {
            try{
                operationLogService.save(operationLog);
                log.info("异步保存操作日志成功");
            } catch (Exception e) {
                log.error("异步保存操作日志失败", e);
            }
        });
    }

}
