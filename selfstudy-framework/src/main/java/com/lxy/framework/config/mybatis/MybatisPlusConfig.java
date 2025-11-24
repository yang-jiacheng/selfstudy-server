package com.lxy.framework.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2021/09/13 13:23
 */
@Configuration
@MapperScan("com.lxy.**.mapper") // 设置mapper接口的扫描包
public class MybatisPlusConfig {

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

    @Bean
    public MybatisPlusInterceptor MybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页拦截器：默认对 left join 进行优化,虽然能优化count,但是加上分页的话如果1对多本身结果条数就是不正确的
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
