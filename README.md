# Minecraft 服务器白名单自助申请系统 (后端)

## 功能简介

前端实现的功能是: 玩家注册登录账号后, 在控制台通过在线答题/高校认证/正版验证获取服务器白名单.

带有网站账号管理、题目管理、白名单管理等功能

后端提供有关网站账号(`user`)、题目(`exam`)、白名单(`whitelist`)、网站配置项(`website`)的内容

API 风格是`RESTful`

## 前端项目地址

[JonjsGithub/whitelist-system-frontend](https://github.com/JonjsGithub/whitelist-system-frontend)

## 所用框架、依赖

使用 Maven 构建
- SpringBoot `3.2.1`
- MyBatisPlus `3.5.5`
- MySQL-Connector-J `8.2.0`
- FastJson `2.0.32`
- Lombok `1.18.30`
- JWT `4.4.0` (用于生成/解密Token)
- jsoup `1.16.1` (用于读取学信网页面)
- org.glavo.rcon-java `2.0.2` (用于连接Minecraft服务器)
- Redis `使用 spring-boot-starter-data-redis` (用于缓存Token验证)

## 运行环境要求
- Java `17+`
- MySQL `8+`
- Redis `任意版本`
- 最低空闲内存 `512 MB` (仅后端java程序, 考虑数据库请预留`1 GB`)
- 最低储存空间 `1 GB`

## 目录结构

```text
src/main/ 源码
|-- java/ Java源代码
    |-- cn/jonjs/whitelistsystembackend/ 包
        |-- WhitelistSystemBackendApplication.java 主类
        |-- config/
            |-- GlobalCorsConfig.java CORS跨域配置
            |-- RconConfig.java Rcon配置
            |-- ...
        |-- controller/
            |-- ...
        |-- exception/ 报错处理器
            |-- ...
        |-- interceptor/ 请求拦截器
            |-- ...
        |-- mapper/
            |-- ...
        |-- pojo/
            |-- Exam.java 题目
            |-- Question.java 单个具体问题 (从Exam中来)
            |-- Result.java 自定义结果
            |-- Student.java 高校认证信息
            |-- User.java 用户/账号
            |-- Website.java 网站配置
        |-- service/
            |-- impl/ 实现类
            |-- ...
        |-- util/ 自己写的工具类
            |-- JudgeUtil.java 考试评判工具类
            |-- JWT.java JWT工具类
            |-- MD5Util.java MD5加密工具类
            |-- RedisUtils.java Redis工具类
            |-- StudentUtil.java 学信网验证工具类
            |-- ThreadLocalUtil.java ThreadLocal工具类
            |-- WhitelistUtil.java 白名单工具类
|-- resources/ 资源文件
    |-- static/
    |-- templates/
    |-- application.yml SpringBoot配置文件

pom.xml Maven配置文件
```

## application.yml 详解
```yaml
server:
  port: 8080 # 后端服务开放的端口
  servlet:
    context-path: "/" # 内容路径
spring: # SpringBoot 相关配置
  datasource: # MySQL 驱动
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/whitelist?useSSL=false&useUnicode=true&characterEncoding=utf8
    # MySQL 8 连接的时候一定要加?后面那一大串, 否则连不上
    # MySQL 5.7 可以不加?后面那一串
    username: root
    password: root
  data:
    redis: # Redis 数据库相关配置
      host: localhost
      port: 6379
      username: default #默认用户名是 default
      password: ""
rcon: # Minecraft Rcon 相关配置
  host: localhost
  port: 25575
  password: ""
  # 对应 server.properties > rcon.password, 可空
time: # JWT 过期时间
  token: 720 # 登录token过期时间 单位分钟
  vcode: 60 # 找回密码过期时间 单位分钟
aliyun:
  # 阿里云账号密钥 ↓
  access-key-id: ""
  access-key-secret: ""
  mail-html: |
    <div style="border:2px solid #9400D3;">
    <span style="font-size: 36px; color: black; padding-left: 16px">五彩世界白名单自助申请系统</span>
    <br />
    <span style="font-size: 24px; color: purple;  padding-left: 16px">【找回密码】</span>
    <br />
    <p style="font-size: 18px;  padding-left: 16px">
    你好, {name}! 你正在通过网页找回您的白名单申请系统密码.
    <br />
    请在 <span style="color: red; font-size: 21px;">{expire}分钟内</span> 完成找回密码的所有操作!
    <br />
    点击或复制下面的链接访问来重置密码:
    </p>
    <a style="font-size: 16px; padding-left: 16px" href="https://whitelist.jonjs.cn/resetPassword/{vcode}" 
    target="_blank">
    https://whitelist.jonjs.cn/resetPassword/{vcode}
    </a>
    <br />
    </div>

```

## API 文档

[APIDOC.md](APIDOC.md)

## MySQL 数据表信息

![1.png](/docimg/1.png)

![2.png](/docimg/2.png)

![3.png](/docimg/3.png)

![4.png](/docimg/4.png)

![5.png](/docimg/5.png)

![6.png](/docimg/6.png)
