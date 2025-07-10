package com.lxy.framework.manager;


import com.lxy.common.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * 应用退出时清理资源
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/03/07 15:40
 */

@Slf4j
@Component
public class SpringShutdownManager implements DisposableBean {

    @Override
    public void destroy() {
        log.info("SpringShutdownManager destroy");
        ThreadPoolUtil.shutdown();
    }
}
