## **selfstudy-server**

**English | [中文](https://github.com/yang-jiacheng/selfstudy-server/blob/master/README.zh-CN.md)**

### 1. Introduction

With the development of internet technology, traditional offline study rooms are increasingly revealing limitations in time and space. This is especially important for students who study at home or have special needs. The "TuanTuan Cloud Study Room" provides a convenient learning platform for students, helping them study efficiently from anywhere.

The platform offers a variety of study modes. Students can create or join public study rooms, participate in focused sessions, set learning goals, etc. By studying together with peers, students can effectively improve their learning efficiency and cultivate focus.


### 2. Technology Stack

jdk 17+

- Core Framework：[Spring Boot 3.4.2](https://github.com/spring-projects/spring-boot)
- Security Framework：[Spring Security](https://github.com/spring-projects/spring-security)
- ORM Framework：[Mybatis Plus 3.5.7](https://github.com/baomidou/mybatis-plus)
- Database：[MySQL 8.0](https://github.com/mysql/mysql-server)
- NoSQL Cache：[Redis 6.2](https://github.com/redis/redis)
- Message Queue：[RabbitMQ 3.10](https://github.com/rabbitmq/rabbitmq-server)

### 3. Cloud Services

- Alibaba Cloud SMS Service
- Alibaba Cloud RDS MySQL 8
- Alibaba Cloud Object Storage Service (OSS)
- Alibaba Cloud Content Delivery Network (CDN)

### 4. Structure

```txt
selfstudy-server    
├── selfstudy-common                     // Define base enumerations, utility classes, constants, annotations, configurations
│    └── com.lxy.common
│         └── annotation                    // Custom annotations
│         └── constant                      // Universal constant
│         └── domain                        // Domain model
│         └── enums                         // Generic enumeration
│         └── properties                    // Custom properties in 'application'
│         └── util                          // Tool class
│         └── vo                            // Domain model encapsulation
├── selfstudy-framework                  // Framework core 
│    └── com.lxy.framework
│         └── aspectj                       // AOP
│         └── config                        // System configuration
│         └── controller                    // Universal controller
│         └── event                         // Spring event
│         └── exception                     // Exception handling
│         └── manager                       // Asynchronous processing
│         └── security                      // Login authentication, permission control
├── selfstudy-system                     // System business
│    └── com.lxy.system
│         └── dto                           // Data transfer object
│         └── engine                        // Template engine
│         └── po                            // Entity Class
│         └── mapper                        // Persistence mapping
│         └── service                       // Business
│         └── vo                            // Value object
│    └── GeneratorMybatisPlus.java          // MybatisPlus code generator
├── selfstudy-admin                     // Background management system server
├── xxl-job-admin                       // XXL-JOB Background management system 
├── selfstudy-mobile-app                // APP server
├── start-server.sh                     // Startup script
```

### 5. Project Link

| Platform   | selfstudy-server（Backend）                          | StudyRoom（Android）              |
| ------ | ------------------------------------------------- | ----------------------------------- |
| github | https://github.com/yang-jiacheng/selfstudy-server | https://github.com/yang-jiacheng/StudyRoom |

### 6. Demo Link

Admin Backend：http://115.29.185.30/selfStudyAdmin/login

App Server：http://115.29.185.30/selfStudyApp/

### 7. Deployment

- Project Root Execute Packaging Commands

```shell
mvn clean package
```

- Server Deployment

The suggested structure is as follows, with the startup script in the project root directory：

```txt
/java     
├── /selfstudy-admin/           
│       └── selfstudy-admin.jar           // Background management system jar package
├── /selfstudy-mobile-app/
│       └── selfstudy-mobile-app.jar      // APP jar package
├── /start-server.sh                      // Startup script
```

- Deployment command

```shell
# start | restart | stop
# The `selfstudy-admin` is the name of the jar package without suffix, or it can be written as `all`, that is, it is executed on all jar packages.
./start-server.sh start selfstudy-admin
```

### Contact Me
For any inquiries, please contact: jiacheng yang

WeChat：crushed_whiskey

Email：yjc1529425632@gmail.com

### License

Selfstudy-server is licensed under the Apache License, Version 2.0. See [LICENSE](https://github.com/yang-jiacheng/selfstudy-server/blob/master/LICENSE) for the full license text.
