package com.lxy.framework.config.mybatis;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.lxy.common.util.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 自定义id生成器
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Configuration
public class CustomerIdGenerator implements IdentifierGenerator {

    @Override
    public Long nextId(Object entity) {
        return IdGenerator.generateId();
    }

    /**
     * 自定义id生成器, 只生成16位的id, 防止前端的无法完整接收
     *
     * @author jiacheng yang.
     * @since 2025/4/27 12:41
     */
    @Primary
    @Bean
    public IdentifierGenerator idGenerator() {
        return new CustomerIdGenerator();
    }

}
