# Affirmation Counter App

一个基于 Spring Boot 和 React 的正向断言计数器应用，帮助用户记录和管理每日正向断言，通过计数功能追踪坚持进度，并支持成就系统和 NFT 铸造。

## 📋 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [功能特性](#功能特性)
- [快速开始](#快速开始)
  - [前置要求](#前置要求)
  - [环境配置](#环境配置)
  - [启动项目](#启动项目)
- [API 文档](#api-文档)
- [开发指南](#开发指南)
- [部署](#部署)
- [贡献](#贡献)

## 🎯 项目简介

Affirmation Counter 是一个全栈应用，旨在帮助用户：
- 创建和管理正向断言（最多 5 条）
- 通过计数器追踪坚持次数
- 达成里程碑成就
- 将成就铸造为 NFT

项目采用前后端分离架构，后端使用 Spring Boot 构建 RESTful API，前端使用 React + Ant Design 构建用户界面。

## 🛠 技术栈

### 后端
- **框架**: Spring Boot 3.3.3
- **语言**: Java 17
- **安全**: Spring Security + JWT + OAuth2 Resource Server
- **数据库**: MySQL 8.0
- **ORM**: MyBatis Plus 3.5.6
- **缓存**: Redis 7
- **数据库迁移**: Flyway
- **监控**: Spring Boot Actuator
- **构建工具**: Maven

### 前端
- **框架**: React 18.2.0
- **构建工具**: Vite 5.4.3
- **UI 组件库**: Ant Design 5.21.2
- **HTTP 客户端**: Axios 1.7.7
- **语言**: TypeScript 5.6.2

### 基础设施
- **容器化**: Docker & Docker Compose
- **数据库**: MySQL 8.0
- **缓存**: Redis 7

## 📁 项目结构

```
affirm/
├── affirm-backend/           # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/affirm/
│   │       │   ├── config/      # 配置类（Security, MyBatis等）
│   │       │   ├── controller/   # REST 控制器
│   │       │   ├── service/      # 业务逻辑层
│   │       │   ├── mapper/       # MyBatis Mapper
│   │       │   ├── domain/       # 实体类
│   │       │   ├── dto/          # 数据传输对象
│   │       │   ├── exception/    # 异常处理
│   │       │   └── util/         # 工具类
│   │       └── resources/
│   │           ├── application.yml    # 应用配置
│   │           └── db/migration/     # Flyway 数据库迁移脚本
│   ├── Dockerfile              # Docker 镜像构建文件
│   └── pom.xml                 # Maven 依赖配置
├── affirm-frontend/           # 前端项目
│   ├── src/
│   │   └── main.tsx           # React 入口文件
│   ├── index.html
│   ├── vite.config.ts
│   └── package.json
├── docker/
│   ├── docker-compose.yml     # Docker Compose 配置
│   └── mysql-init/            # MySQL 初始化脚本
├── API.md                     # API 接口文档
├── LOGIN_TUTORIAL.md          # 登录功能实现教程
└── README.md                  # 项目说明文档
```

## ✨ 功能特性

### 1. 用户认证
- ✅ 用户注册（密码 BCrypt 加密）
- ✅ JWT Token 认证登录
- ✅ 会话管理（登录/退出）
- ✅ 基于 Spring Security 的权限控制

### 2. 正向断言管理
- ✅ 创建正向断言（最多 5 条）
- ✅ 查看断言列表
- ✅ 删除断言

### 3. 计数器功能
- ✅ 查询累计计数
- ✅ 自增计数器（PATCH 语义）
- ✅ 里程碑追踪

### 4. 成就系统
- ✅ 成就列表查询
- ✅ 里程碑成就达成
- ✅ NFT 铸造任务管理

### 5. 其他特性
- ✅ RESTful API 设计
- ✅ 统一异常处理（RFC 7807 Problem+JSON）
- ✅ 数据库版本迁移（Flyway）
- ✅ Redis 缓存支持
- ✅ Docker 容器化部署

## 🚀 快速开始

### 前置要求

- JDK 17+
- Maven 3.6+
- Node.js 18+
- Docker & Docker Compose（可选，用于快速启动数据库和 Redis）

### 环境配置

1. **克隆项目**
```bash
git clone <repository-url>
cd affirm
```

2. **启动基础设施（使用 Docker Compose）**
```bash
cd docker
docker-compose up -d
```

这将启动：
- MySQL 8.0（端口 3306）
- Redis 7（端口 6379）

3. **配置后端**

编辑 `affirm-backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/affirm?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: affirm
    password: secret
  redis:
    host: localhost
    port: 6379

app:
  jwt:
    secret: your-secret-key-change-this-in-production-min-256-bits-please-use-a-secure-random-key
```

### 启动项目

#### 后端

```bash
cd affirm-backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动。

#### 前端

```bash
cd affirm-frontend
npm install
npm run dev
```

前端开发服务器将在 `http://localhost:5173` 启动。

### 验证安装

访问 `http://localhost:8080/ping` 检查后端是否正常运行。

## 📚 API 文档

详细的 API 文档请参考 [API.md](./API.md)。

### 主要接口概览

#### 认证
- `POST /auth/register` - 用户注册
- `POST /sessions` - 用户登录
- `DELETE /sessions/me` - 退出登录

#### 正向断言
- `GET /affirm` - 获取断言列表
- `POST /affirm` - 创建断言（最多 5 条）
- `DELETE /affirm/{id}` - 删除断言

#### 计数器
- `GET /counter` - 查询累计次数
- `PATCH /counter` - 自增计数器

#### 成就
- `GET /achi` - 获取成就列表
- `POST /achi/{id}/mint` - 铸造 NFT

### 认证方式

所有需要认证的接口都需要在请求头中包含 JWT Token：

```
Authorization: Bearer <your-jwt-token>
```

## 💻 开发指南

### 后端开发

1. **代码结构**
   - Controller 层：处理 HTTP 请求
   - Service 层：业务逻辑实现
   - Mapper 层：数据库操作（MyBatis Plus）
   - DTO：数据传输对象
   - Domain：实体模型

2. **数据库迁移**
   - 迁移脚本位于 `src/main/resources/db/migration/`
   - 使用 Flyway 管理数据库版本
   - 命名规范：`V{version}__{description}.sql`

3. **安全配置**
   - JWT 配置：`config/SecurityConfig.java`
   - JWT 服务：`service/JwtService.java`
   - 登录功能教程：参见 [LOGIN_TUTORIAL.md](./LOGIN_TUTORIAL.md)

### 前端开发

1. **技术栈**
   - React 18 + TypeScript
   - Ant Design 组件库
   - Vite 构建工具

2. **开发命令**
```bash
npm run dev      # 启动开发服务器
npm run build    # 构建生产版本
npm run preview  # 预览生产构建
```

## 🐳 部署

### Docker 部署

#### 构建后端镜像

```bash
cd affirm-backend
docker build -t affirm-backend:latest .
```

#### 使用 Docker Compose（推荐）

可以扩展 `docker/docker-compose.yml` 添加应用服务：

```yaml
services:
  backend:
    build: ../affirm-backend
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/affirm
      - SPRING_REDIS_HOST=redis
```

### 生产环境配置

⚠️ **重要**: 生产环境请务必修改以下配置：

1. **JWT Secret**: 使用足够长度的随机密钥（至少 256 位）
2. **数据库密码**: 使用强密码
3. **HTTPS**: 配置 SSL/TLS 证书
4. **环境变量**: 敏感信息使用环境变量而非配置文件

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证。

## 📞 联系方式

如有问题或建议，请提交 Issue。

---

**Happy Coding! 🎉**

