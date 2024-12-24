package com.lxy.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description: 程序入口
 * @author: jiacheng yang.
 * @Date: 2022/10/08 20:55
 * @Version: 1.0
 * nohup java -jar -Xms500m -Xmx500m selfstudy-app.jar --spring.profiles.active=prod > /dev/null 2>&1 &
 */

//@ComponentScan("com.lxy")
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.lxy"})
public class SelfStudyMobileApp {

    public static void main(String[] args) {
        SpringApplication.run(SelfStudyMobileApp.class, args);
    }

}
