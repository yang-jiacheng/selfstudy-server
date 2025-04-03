## **selfstudy-server**

**[English](https://github.com/yang-jiacheng/selfstudy-server/blob/master/README.md) | 中文**

### 1. 简介

随着互联网技术的发展，传统的线下自习室逐渐暴露出时间和空间的局限性，尤其对于在家学习或有特殊需求的学生而言，线上自习室显得尤为重要。团团云自习为广大学生提供了一个便捷的学习平台，帮助他们在任何地方高效学习。

平台提供多样化的自习形式，学生可以创建或加入公共自习室、参与专注时段、设置学习目标等。通过与同学们共同学习，能够有效提高学习效率，培养专注力。



### 2. 技术选型

jdk 17+

- 核心框架：[Spring Boot 3.4.2](https://github.com/spring-projects/spring-boot)
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
- 阿里云ECS服务器

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
│         └── exception                     // 异常处理
│         └── manager                       // 异步处理
│         └── security                      // 登录认证，权限控制
├── selfstudy-system                     // 系统业务
│    └── com.lxy.system
│         └── dto                           // 数据传输对象
│         └── engine                        // 模板引擎
│         └── event                         // spring事件
│         └── po                            // 实体类
│         └── mapper                        // 持久化映射
│         └── service                       // 业务
│         └── vo                            // 值对象
│    └── GeneratorMybatisPlus.java          // MybatisPlus代码生成器
├── selfstudy-admin                     // 后管服务端
├── selfstudy-mobile-app                // APP服务端
├── start-server.sh                     // 启动脚本
```

### 5. 项目地址

| 平台   | selfstudy-server（后端）                          | StudyRoom（Android端）              |
| ------ | ------------------------------------------------- | ----------------------------------- |
| github | https://github.com/yang-jiacheng/selfstudy-server | https://github.com/yang-jiacheng/StudyRoom |

### 6. 演示地址

后台管理系统：http://115.29.185.30/selfStudyAdmin/login
App 服务端：http://115.29.185.30/selfStudyApp/

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
├── /start-server.sh                      // 启动脚本
```

- 部署命令

```shell
# start | restart | stop
# `selfstudy-admin`为jar包不带后缀的名称，也可写为`all`，就是对所有jar执行
./start-server.sh start selfstudy-admin
```

### License

```license
Copyright 2022 jiacheng yang.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

如有任何问题请联系：杨嘉诚

微信号：crushed_whiskey

邮箱：yjc1529425632@gmail.com
