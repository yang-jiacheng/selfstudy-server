package com.lxy.framework.manager;


import com.lxy.common.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * 应用退出时清理资源
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/03/07 15:40
 */

@Component
public class SpringShutdownManager implements DisposableBean {
    private final static Logger LOG = LoggerFactory.getLogger(SpringShutdownManager.class);

    @Override
    public void destroy() {
        LOG.info("SpringShutdownManager destroy");
        ThreadPoolUtil.shutdown();
    }
}
