## **selfstudy-server**

**[English](https://github.com/yang-jiacheng/selfstudy-server/blob/master/README.md) | 中文**

### 1. 简介

随着互联网技术的发展，传统的线下自习室逐渐暴露出时间和空间的局限性，尤其对于在家学习或有特殊需求的学生而言，线上自习室显得尤为重要。团团云自习为广大学生提供了一个便捷的学习平台，帮助他们在任何地方高效学习。

平台提供多样化的自习形式，学生可以创建或加入公共自习室、参与专注时段、设置学习目标等。通过与同学们共同学习，能够有效提高学习效率，培养专注力。



### 2. 技术选型

jdk 17+

- 核心框架：[Spring Boot 3.4.5](https://github.com/spring-projects/spring-boot)
- 安全框架：[Spring Security](https://github.com/spring-projects/spring-security)
- ORM 框架：[Mybatis Plus 3.5.7](https://github.com/baomidou/mybatis-plus)
- 数据库：[MySQL 8.0](https://github.com/mysql/mysql-server)
- NoSQL 缓存：[Redis 6.2](https://github.com/redis/redis)
- 消息队列：[RabbitMQ 3.10](https://github.com/rabbitmq/rabbitmq-server)

### 3. 云服务

- 阿里云短信服务
- 阿里云RDS数据库mysql 8
- 阿里云对象存储（OSS）
- 阿里云内容分发网络（CDN）

### 4. 结构

```txt
selfstudy-server    
├── selfstudy-common                     // 定义基础 枚举、工具类、常量、注解、配置
│    └── com.lxy.common
│         └── annotation                    // 自定义注解
│         └── constant                      // 通用常量
│         └── domain                        // 领域模型
│         └── enums                         // 通用枚举
│         └── properties                    // application中的自定义属性
│         └── util                          // 工具类
│         └── vo                            // 领域模型封装
├── selfstudy-framework                  // 框架核心
│    └── com.lxy.framework
│         └── aspectj                       // AOP
│         └── config                        // 系统配置
│         └── controller                    // 通用控制器
│         └── event                         // spring事件
│         └── exception                     // 异常处理
│         └── manager                       // 异步处理
│         └── security                      // 登录认证，权限控制
├── selfstudy-system                     // 系统业务
│    └── com.lxy.system
│         └── dto                           // 数据传输对象
│         └── engine                        // 模板引擎
│         └── po                            // 实体类
│         └── mapper                        // 持久化映射
│         └── service                       // 业务
│         └── vo                            // 值对象
│    └── GeneratorMybatisPlus.java          // MybatisPlus代码生成器
├── selfstudy-admin                     // 后管服务端
├── xxl-job-admin                       // XXL-JOB 后管
├── selfstudy-mobile-app                // APP服务端
├── start-server.sh                     // 启动脚本
```

### 5. 项目地址

- **服务端:** https://github.com/yang-jiacheng/selfstudy-server
- **后台管理系统:** https://github.com/yang-jiacheng/studyroom-admin-web
- **Android 客户端:** https://github.com/yang-jiacheng/StudyRoom

### 6. 演示地址

后台管理系统：http://115.29.185.30/selfStudyAdmin/login

演示账号：管理员：yjc123/123456；其他角色：xiuyu123/123456

App 服务端：http://115.29.185.30/selfStudyApp/

演示账号：17508660924/123456

由于阿里云SMS短信服务限制，个人开发者无法申请短信签名，所以请使用密码登录

### 7. 打包

- 项目根目录执行打包命令

```shell
mvn clean package
```

- 服务器部署

建议结构如下，启动脚本在项目根目录：

```txt
/java     
├── /selfstudy-admin/           
│       └── selfstudy-admin.jar           // 后管jar包
├── /selfstudy-mobile-app/
│       └── selfstudy-mobile-app.jar      // APP jar包
├── /xxl-job-admin/
│       └── xxl-job-admin.jar 
├── /start-server.sh                      // 启动脚本
```

- 部署命令

```shell
# start | restart | stop
# `selfstudy-admin`为jar包不带后缀的名称，也可写为`all`，就是对所有jar执行
./start-server.sh start selfstudy-admin
```

### 联系我

如有任何问题请联系：杨嘉诚

微信号：crushed_whiskey

邮箱：yjc1529425632@gmail.com

### License

Selfstudy-server 是根据Apache许可证2.0版获得许可的。有关完整的许可证文本，请参阅[LICENSE](https://github.com/yang-jiacheng/selfstudy-server/blob/master/LICENSE)
