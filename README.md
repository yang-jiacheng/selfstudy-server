## **selfstudy-server**

**English | [中文](https://github.com/yang-jiacheng/selfstudy-server/blob/master/README.zh-CN.md)**

### 1. Introduction

With the development of internet technology, traditional offline study rooms are increasingly revealing limitations in time and space. This is especially important for students who study at home or have special needs. The "TuanTuan Cloud Study Room" provides a convenient learning platform for students, helping them study efficiently from anywhere.

The platform offers a variety of study modes. Students can create or join public study rooms, participate in focused sessions, set learning goals, etc. By studying together with peers, students can effectively improve their learning efficiency and cultivate focus.

### 2. Core Features

#### 🚀 **Learning Management**
- **Smart Study Rooms**: Support creating/joining public study rooms with real-time online user display
- **Multiple Timing Modes**: Countdown and count-up timing modes to suit different study habits
- **Study Records**: Automatic recording of study duration and notes, generating detailed study reports
- **Seat Management**: Virtual seat allocation to create authentic study room atmosphere

#### 👥 **Social Interaction**
- **Study Leaderboards**: Daily/weekly/monthly study duration rankings to motivate continuous learning
- **Study Note Sharing**: Support text and image notes to promote knowledge exchange
- **User Feedback**: Comprehensive feedback mechanism for continuous user experience optimization

#### 🔐 **Security Authentication**
- **Dual Token Authentication**: Admin backend uses Access Token + Refresh Token dual protection
- **Single Token Authentication**: Mobile app uses JWT single token authentication, balancing security and convenience
- **Permission Management**: Fine-grained permission control system based on RBAC

#### 📊 **Data Statistics**
- **Study Analytics**: Personal study duration, focus analysis, and progress trend analysis
- **Real-time Monitoring**: System operation status and user activity real-time monitoring
- **Data Export**: Support Excel export of study data and user data

#### ⚙️ **System Management**
- **Business Configuration**: Dynamic configuration management with runtime parameter adjustment
- **Version Management**: APP version release and update management
- **Scheduled Tasks**: Distributed task scheduling based on XXL-JOB
- **Audit Logging**: Complete operation log recording and querying

### 3. Technology Stack

jdk 17+

- Core Framework：[Spring Boot 3.5.7](https://github.com/spring-projects/spring-boot)
- Security Framework：[Spring Security](https://github.com/spring-projects/spring-security)
- ORM Framework：[Mybatis Plus 3.5.7](https://github.com/baomidou/mybatis-plus)
- Database：[MySQL 8.0.36](https://github.com/mysql/mysql-server)
- NoSQL Cache：[Redis 6.2](https://github.com/redis/redis)
- Message Queue：[RabbitMQ 4.1.0](https://github.com/rabbitmq/rabbitmq-server)

### 4. Cloud Services

- Alibaba Cloud SMS Service
- Alibaba Cloud RDS MySQL 8
- Alibaba Cloud Object Storage Service (OSS)
- Alibaba Cloud Content Delivery Network (CDN)

### 5. System Architecture

#### Technical Architecture Features
- **Modular Design**: High cohesion and low coupling module division for easy maintenance and expansion
- **Event-Driven**: Asynchronous event processing mechanism based on Spring Event
- **Caching Strategy**: Multi-level cache design to improve system response speed
- **Monitoring System**: Complete operation audit chain with AOP + event listening

#### Security Architecture
```
┌─────────────────┐    ┌──────────────────┐
│   Admin Auth    │    │   Mobile Auth    │
│ AccessToken(15m)│    │   JWT Token      │
│RefreshToken(30d)│    │   (Long-term)    │
└─────────────────┘    └──────────────────┘
         │                       │
         └───────────┬───────────┘
                     │
            ┌─────────────────┐
            │  Spring Security│
            │ Unified Security │
            └─────────────────┘
```

### 6. Structure

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

### 7. Quick Start

1. Import SQL script `docs/selfstudy.sql`
2. Configure `application-dev.properties`
3. Access URLs
   - Admin API: http://localhost:8071/selfStudyAdmin
   - Mobile API: http://localhost:8072/selfStudyApp
   - XXL-JOB: http://localhost:8060/xxl-job-admin

### 8. Project Links

#### 🔗 Related Repositories

- **🖥️ Server:** https://github.com/yang-jiacheng/selfstudy-server
- **🌐 Admin Web:** https://github.com/yang-jiacheng/studyroom-admin-web  
- **📱 Android Client:** https://github.com/yang-jiacheng/StudyRoom

#### 🏗️ Project Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     Cloud Study Ecosystem                   │
├─────────────────────────────────────────────────────────────┤
│  📱 Android App          🌐 Admin Web         🖥️ Backend    │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐ │
│  │   User App      │    │   Admin Panel   │    │   API Server    │ │
│  │                │    │                │    │                │ │
│  │ • Room Booking  │◄──►│ • User Mgmt     │◄──►│ • Spring Boot   │ │
│  │ • Study Records │    │ • Statistics    │    │ • MySQL DB      │ │
│  │ • Profile       │    │ • System Config │    │ • Redis Cache   │ │
│  │ • Notifications │    │ • Permission    │    │ • OSS Storage   │ │
│  └─────────────────┘    └─────────────────┘    └─────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### 9. Demo Links

#### 🌐 Online Experience

**Admin Backend：** http://115.29.185.30/studyRoomAdminWeb/#/

**Demo Account：**

`test123` / `123456`
> Note: The demo account only has query permission

**Android Client Download：** [Click to download](https://selfstudy-server.oss-cn-hangzhou.aliyuncs.com/android/studyroom/apk/studyroom-1.0.6-7-0125.apk)


#### 📱 Feature Preview

You can try the following features during the experience:

- ✅ User login and permission switching
- ✅ Dynamic menus and routing
- ✅ User management and role assignment  
- ✅ Study record queries and statistics
- ✅ System configuration and parameter management
- ✅ OSS file upload and storage management
- ✅ Responsive layout adaptation

### 10. Deployment

#### Local Packaging
Execute packaging commands in the project root directory

```shell
mvn clean package
```

#### Server Deployment
The suggested structure is as follows, with the startup script in the project root directory：

```txt
/java     
├── /logs/
│       └── gc/                           // gc log directory
│       └── heap-dumps/                   // Automatic dump heap file directory when OOM occurs, must have
├── /selfstudy-admin/           
│       └── selfstudy-admin.jar           // Background management system jar package
├── /selfstudy-mobile-app/
│       └── selfstudy-mobile-app.jar      // APP jar package
├── /start-server.sh                      // Startup script
```

#### Deployment Commands
```shell
# start | restart | stop
# The `selfstudy-admin` is the name of the jar package without suffix, or it can be written as `all`, that is, it is executed on all jar packages.
./start-server.sh start selfstudy-admin
```

### Contact Me

#### 👨‍💻 Author Information

**Jiacheng Yang** - Full Stack Developer

**Contact:**

- 📧 **Email:** jace.yang.dev@gmail.com
- 💬 **WeChat:** crushed_whiskey
- 🐙 **GitHub:** https://github.com/yang-jiacheng

**About Author:**

- 💼 Focus on full-stack development and system architecture
- 🌟 Passionate about open source projects and technology sharing
- 🎯 Committed to building elegant and efficient software systems

#### 💡 Feedback & Suggestions

If you have any questions, suggestions or feedback, please contact us through:

1. **GitHub Issues** - Report bugs or feature requests
2. **Email Contact** - Technical communication and cooperation
3. **WeChat Consultation** - Quick response and technical support

### License

Selfstudy-server is licensed under the Apache License, Version 2.0. See [LICENSE](https://github.com/yang-jiacheng/selfstudy-server/blob/master/LICENSE) for the full license text.

---

<div align="center">
	<p><strong>⭐ If this project helps you, please give me a Star! ⭐</strong></p>
    <p><strong>📢 Welcome to share with more friends in need! 📢</strong></p>
</div>
