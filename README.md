

FONTANA 开发框架
===============

当前最新版本： 1.2.1-SNAPSHOT（发布日期：2022-10-8）


项目介绍
-----------------------------------

<h3 align="center">Java Development Framework for Enterprise web applications</h3>

Fontana是一款基于Java生态的“开箱即用”的开发技术框架，通过对一些通用功能的封装，方便用户基于此快速构建应用程序。

`FONTANA宗旨是:` 简单、实用、可配置、低侵入性！


后台目录结构
-----------------------------------
```
项目结构
├─fontana（父POM： 项目依赖、modules组织）
│  ├─fontana-base    基础模块： 基类、常量、注解等
│  ├─fontana-demo    示例代码
│  ├─fontana-util    工具类
│  ├─fontana-db-starter    基于MP封装的基础crud操作
│  ├─fontana-log-starter   基于logback封装的日志组件
│  ├─fontana-demo    示例代码
│  │  ├─feign-demo    openfeign使用示例
│  │  ├─xxljob-demo   xxljob使用示例
│  │  ├─...    不一一列举了
│  ├─fontana-multitenancy   多租户隔离组件，支持数据表级和数据库实例级数据隔离
│  ├─fontana-redis-starter  基于redisTemplate和redisson封装的缓存工具类，支持分布式锁
│  ├─fontana-rabbitmq-starter 封装的rabbitmq客户端
│  ├─fontana-oss  封装基于aws s3协议的对象存储（七牛oss、阿里云oss、minio等）
│  ├─fontana-xxljob-starter 基于xxljob封装的分布式任务调度组件
│  ├─fontana-swagger2-starter 基于knife4j的接口文档管理
│  ├─fontana-sb-starter  spingboot项目基础配置，统一异常处理、统一跨域处理、国际化支持
│  ├─fontana-cloud-starter  微服务项目基础配置
```

技术架构：
-----------------------------------
#### 开发环境

- 语言：Java 8+ (小于17)

- IDE(JAVA)： IDEA (必须安装lombok插件 )

- IDE(前端)： Vscode、WebStorm、IDEA

- 依赖管理：Maven

- 缓存：Redis

- 数据库脚本：MySQL5.7+ 
    

#### 后端

- 基础框架：Spring Boot 2.3.12.RELEASE

- 微服务框架： Spring Cloud Hoxton.SR12 & Spring Cloud Alibaba 2.2.7.RELEASE

- 持久层框架：MybatisPlus 3.4.1

- 微服务技术栈：Spring Cloud、Spring Cloud Alibaba、Nacos、Gateway、Sentinel、Skywalking

- 数据库连接池：阿里巴巴Druid 1.2.5

- 日志打印：logback

- 其他：easypoi,fastjson,Swagger-ui, lombok（简化代码）等。




## 微服务解决方案

1、服务注册和发现 Nacos √

2、统一配置中心 Nacos  √

3、路由网关 gateway(三种加载方式) √

4、分布式 http feign √

5、熔断降级限流 Sentinel √

6、分布式文件 Minio、阿里OSS √

7、统一权限控制 JWT √

8、服务监控 SpringBootAdmin√

9、链路跟踪 Skywalking  

10、消息中间件 RabbitMQ  √

11、分布式任务 xxl-job  √

12、分布式事务 Seata

13、分布式日志 elk + kafka

14、支持 docker-compose、k8s、jenkins

15、CAS 单点登录   √

16、路由限流   √


