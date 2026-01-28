## **selfstudy-server**

**[English](https://github.com/yang-jiacheng/selfstudy-server/blob/master/README.md) | 中文**

### 1. 简介

随着互联网技术的发展，传统的线下自习室逐渐暴露出时间和空间的局限性，尤其对于在家学习或有特殊需求的学生而言，线上自习室显得尤为重要。团团云自习为广大学生提供了一个便捷的学习平台，帮助他们在任何地方高效学习。

平台提供多样化的自习形式，学生可以创建或加入公共自习室、参与专注时段、设置学习目标等。通过与同学们共同学习，能够有效提高学习效率，培养专注力。

### 2. 核心功能特性

#### 🚀 **学习管理**
- **智能学习室**: 支持创建/加入公共学习室，实时显示在线学习人数
- **多种计时模式**: 正计时和倒计时两种学习模式，满足不同学习习惯
- **学习记录**: 自动记录学习时长、笔记内容，生成详细的学习报告
- **座位管理**: 虚拟座位分配，营造真实自习室氛围

#### 👥 **社交互动**
- **学习排行榜**: 每日/周/月学习时长排行，激励持续学习
- **学习笔记分享**: 支持文字和图片笔记，促进知识交流
- **用户反馈**: 完善的反馈机制，持续优化用户体验

#### 🔐 **安全认证**
- **双Token认证**: 后台管理系统采用Access Token + Refresh Token双重保护
- **单Token认证**: 移动端使用JWT单Token认证，平衡安全性和便利性
- **权限管理**: 基于RBAC的细粒度权限控制系统

#### 📊 **数据统计**
- **学习统计**: 个人学习时长、专注度、进步趋势分析
- **实时监控**: 系统运行状态、用户活跃度实时监控
- **数据导出**: 支持学习数据和用户数据的Excel导出

#### ⚙️ **系统管理**
- **业务配置**: 动态配置管理，支持运行时参数调整
- **版本管理**: APP版本发布和更新管理
- **定时任务**: 基于XXL-JOB的分布式任务调度
- **日志审计**: 完整的操作日志记录和查询

### 3. 技术栈

jdk 17+

- 核心框架：[Spring Boot 3.4.5](https://github.com/spring-projects/spring-boot)
- 安全框架：[Spring Security](https://github.com/spring-projects/spring-security)
- ORM 框架：[Mybatis Plus 3.5.7](https://github.com/baomidou/mybatis-plus)
- 数据库：[MySQL 8.0](https://github.com/mysql/mysql-server)
- NoSQL 缓存：[Redis 6.2](https://github.com/redis/redis)
- 消息队列：[RabbitMQ 3.10](https://github.com/rabbitmq/rabbitmq-server)

### 4. 云服务

- 阿里云短信服务
- 阿里云RDS数据库mysql 8
- 阿里云对象存储（OSS）
- 阿里云内容分发网络（CDN）

### 5. 系统架构

#### 技术架构特
- **模块化设计**: 高内聚低耦合的模块划分，便于维护和扩展
- **事件驱动**: 基于Spring Event的异步事件处理机制
- **缓存策略**: 多级缓存设计，提升系统响应速度
- **监控体系**: AOP + 事件监听的完整操作审计链路

#### 安全架构
```
┌─────────────────┐    ┌──────────────────┐
│   管理端认证     │    │    移动端认证     │
│ AccessToken(15m)│    │   JWT Token      │
│RefreshToken(30d)│    │    (长期有效)     │
└─────────────────┘    └──────────────────┘
         │                       │
         └───────────┬───────────┘
                     │
            ┌─────────────────┐
            │  Spring Security│
            │   统一权限控制    │
            └─────────────────┘
```

### 6. 结构

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

### 7. 快速开始

1. 导入SQL脚本 `docs/selfstudy.sql`
2. 配置`application-dev.properties`
3. 访问地址
   - 后台管理API: http://localhost:8071/selfStudyAdmin
   - 移动端API: http://localhost:8072/selfStudyApp
   - XXL-JOB: http://localhost:8060/xxl-job-admin

### 8. 项目地址

#### 🔗 相关仓库

- **🖥️ 服务端:** https://github.com/yang-jiacheng/selfstudy-server
- **🌐 后台管理系统:** https://github.com/yang-jiacheng/studyroom-admin-web  
- **📱 Android 客户端:** https://github.com/yang-jiacheng/StudyRoom

#### 🏗️ 项目架构图

```
┌─────────────────────────────────────────────────────────────┐
│                         云自习生态系统                        │
├─────────────────────────────────────────────────────────────┤
│  📱 Android App          🌐 Admin Web         🖥️ Backend    │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐ │
│  │   用户端应用     │    │   管理后台       │    │   API服务器      │ │
│  │                │    │                │    │                │ │
│  │ • 自习室预约    │◄──►│ • 用户管理       │◄──►│ • Spring Boot   │ │
│  │ • 学习记录      │    │ • 数据统计       │    │ • MySQL数据库   │ │
│  │ • 个人中心      │    │ • 系统配置       │    │ • Redis缓存     │ │
│  │ • 消息通知      │    │ • 权限控制       │    │ • OSS存储       │ │
│  └─────────────────┘    └─────────────────┘    └─────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### 9. 演示地址

#### 🌐 在线体验

**后台管理系统：** http://115.29.185.30/studyRoomAdminWeb/#/

**演示账号：**

- 管理员：`yjc123` / `123456`
- 其他角色：`xiuyu123` / `123456`

**Android 客户端下载：** https://selfstudy-server.oss-cn-hangzhou.aliyuncs.com/android/studyroom/apk/studyroom-1.0.6-7-0125.apk

**移动端演示账号：** `17508660924` / `123456`


#### 📱 功能预览

体验时可以尝试以下功能：

- ✅ 用户登录与权限切换
- ✅ 动态菜单和路由
- ✅ 用户管理和角色分配  
- ✅ 学习记录查询和统计
- ✅ 系统配置和参数管理
- ✅ OSS文件上传和存储管理
- ✅ 响应式布局适配

### 10. 打包部署

#### 本地打包
项目根目录执行打包命令

```shell
mvn clean package
```

#### 服务器部署
建议结构如下，启动脚本在项目根目录：

```txt
/java
├── /logs/
│       └── gc/                           // gc日志目录
│       └── heap-dumps/                   // 发生OOM时自动dump堆文件目录，必须有
├── /selfstudy-admin/           
│       └── selfstudy-admin.jar           // 后管jar包
├── /selfstudy-mobile-app/
│       └── selfstudy-mobile-app.jar      // APP jar包
├── /start-server.sh                      // 启动脚本
```

#### 部署命令
```shell
# start | restart | stop
# `selfstudy-admin`为jar包不带后缀的名称，也可写为`all`，就是对所有jar执行
./start-server.sh start selfstudy-admin
```

### 联系我

#### 👨‍💻 作者信息

**杨嘉诚** - 全栈开发工程师

**联系方式：**

- 📧 **邮箱：** yjc1529425632@gmail.com
- 💬 **微信：** crushed_whiskey
- 🐙 **GitHub：** https://github.com/yang-jiacheng

**关于作者：**

- 💼 专注于全栈开发和系统架构
- 🌟 热爱开源项目和技术分享
- 🎯 致力于构建优雅高效的软件系统

#### 💡 反馈与建议

如果您有任何问题、建议或反馈，欢迎通过以下方式联系：

1. **GitHub Issues** - 报告Bug或功能请求
2. **邮件联系** - 技术交流和合作
3. **微信咨询** - 快速响应和技术支持

### License

selfstudy-server 是根据Apache许可证2.0版获得许可的。有关完整的许可证文本，请参阅[LICENSE](https://github.com/yang-jiacheng/selfstudy-server/blob/master/LICENSE)

---

<div align="center">
	<p><strong>⭐ 如果这个项目对您有帮助，请给我一个Star！⭐</strong></p>
    <p><strong>📢 欢迎分享给更多需要的朋友！📢</strong></p>
</div>
