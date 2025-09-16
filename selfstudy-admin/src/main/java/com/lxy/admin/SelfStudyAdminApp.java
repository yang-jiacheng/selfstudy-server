package com.lxy.admin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 程序入口
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/10/08 18:40
 */

@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.lxy"})
public class SelfStudyAdminApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SelfStudyAdminApp.class, args);
        FilterChainProxy bean = run.getBean(FilterChainProxy.class);
        System.out.println("Hello World!");
    }

}
