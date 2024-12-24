## **selfstudy-server**

### 简介

selfstudy-server是 团团自习室 的服务端和对应的后台管理系统。使用 maven 多模块开发 

selfstudy-admin 后台系统 未前后端分离是全栈项目，selfstudy-app为APP的服务端，selfstudy-common为公用依赖。



### 技术选型

- Spring Boot：基于Spring框架，简化了Spring应用的配置和开发过程。
- Spring Security：使用Spring Security来实现用户的认证和授权，包括登录认证、角色权限管理等功能。
- MyBatis-Plus：基于MyBatis框架，简化了MyBatis的开发流程，提供了更加便捷的CRUD操作。
- 阿里云RDS数据库mysql8：具有可扩展性、高可用性、高性能、管理便捷等优点，能够满足团团云自习的服务端对数据管理的需求
- Redis 6：高效的内存数据存储系统，提高了访问速度，减轻了数据库的负载，同时也提高了系统的可扩展性



### 云服务

- 阿里云短信服务：阿里云短信服务是一款可以实现短信验证码等功能的云服务。在团团云自习中，使用阿里云短信服务来实现短信验证码登录|注册的功能。
- 阿里云对象存储（**OSS**）和内容分发网络（**CDN**）：阿里云对象存储和内容分发网络可以实现文件的高速上传和下载，同时提供了全球覆盖的CDN节点，它具有高性能、高可用性、低成本、高扩展性和高安全性等特点，能够帮助开发者快速、安全地实现文件存储和访问。在团团云自习中，使用阿里云对象存储来存储用户上传的文件和图片，使用内容分发网络来提高文件和图片的访问速度和可靠性。
- 阿里云轻量服务器2核4G：性价比非常高。服务端应用都部署在服务器通过nginx反向代理访问



### 项目地址

| 平台  | selfstudy-server（后端）                   | StudyRoom（Android端）              |
| ----- | ------------------------------------------ | ----------------------------------- |
| Gitee | https://gitee.com/oswuhan/selfstudy-server | https://gitee.com/oswuhan/StudyRoom |



### 演示地址

后台管理系统：https://www.xiuyu.life/selfStudyAdmin/login

APP服务端接口文档： https://www.apifox.cn/apidoc/shared-97a135e0-029f-4bee-8db9-813d2298ff2a  

访问密码 : cFO2WWrb 



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

微信号：halou_Java

邮箱：1529425632@qq.com