package com.lxy.admin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 程序入口
 * -Xms:初始堆大小 默认为物理内存的1/64 , -Xmx:最大堆大小 默认为物理内存的1/4
 * -Xms1g -Xmx1g 把两者设置为一致,是为了避免频繁扩容和GC释放堆内存造成的系统开销/压力
 * @author jiacheng yang.
 * @since 2022/10/08 18:40
 * @version 1.0
 * nohup java -jar -Xms500m -Xmx500m selfstudy-admin.jar --spring.profiles.active=prod > /dev/null 2>&1 &
 */

//@ComponentScan("com.lxy")
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.lxy"})
public class SelfStudyAdminApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SelfStudyAdminApp.class, args);
        FilterChainProxy bean = run.getBean(FilterChainProxy.class);
        System.out.println("Hello World!");
    }

}
