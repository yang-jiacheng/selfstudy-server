package com.lxy.admin.task;

import cn.hutool.core.thread.ThreadUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
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
public class AdminTask {

    /**
     * 测试admin任务
     *
     * @author jiacheng yang.
     * @since 2025/5/9 15:08
     */
    @XxlJob("testAdminTask")
    public void testAdminTask() {
        log.info("测试admin任务开始");
        ThreadUtil.sleep(3000);
        log.info("测试admin任务结束");
    }

}
