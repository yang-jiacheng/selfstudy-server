package com.lxy.app;

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
 * nohup java -jar -Xms500m -Xmx500m selfstudy-app.jar --spring.profiles.active=prod > /dev/null 2>&1 &
 * @since 2022/10/08 20:55
 */

@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.lxy"})
public class SelfStudyMobileApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SelfStudyMobileApp.class, args);
        FilterChainProxy bean = applicationContext.getBean(FilterChainProxy.class);
        System.out.println("Hello World!");
    }

}
