package com.lxy.framework.event;

import com.lxy.system.po.OperationLog;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 * 操作日志事件
 * @author jiacheng yang.
 * @since  2022/12/20 10:49
 * @version  1.0
 */

@Getter
public class OperationLogEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 1163187033881974423L;

    private final OperationLog operationLog;

    public OperationLogEvent(Object source, OperationLog operationLog) {
        super(source);
        this.operationLog = operationLog;
    }

}
