# affirm
## Overview｜项目概述

**Affirmation Counter DApp** 是一个融合 **心理激励机制 + Web3 链上奖励** 的个人成长项目。

用户可以通过点击按钮或手机音量键记录每日肯定语（Affirmations），系统会自动统计次数、触发成就，并在达成指定里程碑时自动在链上 mint NFT 勋章。

项目还集成了 AI 模块，每天为用户生成专属的积极肯定语句。

---

## Tech Stack｜技术栈

| 层级 | 技术 | 说明 |
| --- | --- | --- |
| Backend | **Spring Boot**, MyBatis-Plus, Redis, JWT, EventBus | 核心 API 服务与缓存逻辑 |
| Database | **MySQL**, Flyway | 数据持久化与迁移管理 |
| Blockchain | **Solidity**, Web3j, Polygon Testnet | 铸造 NFT 勋章与链上交互 |
| Frontend | **React**, Vite, Ant Design, Axios | 页面交互与可视化展示 |
| AI Module | LangChain4j + OpenAI API | 每日自动生成 Affirmation |
| DevOps | Docker, Docker Compose, Nginx | 容器化与统一部署 |

---

## Core Features｜核心功能

### 1. Affirmation Recording

- 点击按钮或手机音量键即可 +1；
- Redis 实现高并发原子计数；
- 定时任务落库，保证数据不丢。

### 2. Achievement System

- 当 affirm 次数达到 5000 / 7500 / 10000 自动解锁成就；
- 事件驱动触发链上 mint；
- 成就数据写入 MySQL 并持久保存。

### 3. On-chain NFT Reward

- Solidity ERC-721 合约：`mintTo(address, milestone)`；
- 由后端 Web3j 调用 Polygon 测试网；
- 每个 NFT 附带用户、次数、时间戳等元数据。

### 4. AI Affirmation Generator

- 每日定时生成个性化的 Affirmation；
- 使用 LangChain4j 调用 OpenAI API；
- 存储于数据库中，可在前端展示。

### 5. Dockerized Deployment

- 后端、数据库、缓存、前端全部容器化；
- 一键启动：`docker compose up -d`；
- Nginx 反向代理统一路由。

---

---

## API Specification｜接口说明

| 方法 | 路径 | 功能 |
| --- | --- | --- |
| `POST` | `/api/v1/auth/register` | 用户注册 |
| `POST` | `/api/v1/auth/login` | 登录并返回 JWT |
| `GET` | `/api/v1/affirmations` | 查询肯定语列表 |
| `POST` | `/api/v1/affirmations` | 新增肯定语（最多 5 条） |
| `POST` | `/api/v1/counter/incr` | 点击 +1，返回最新统计 |
| `GET` | `/api/v1/counter` | 查询累计次数 |
| `GET` | `/api/v1/achievements` | 查看解锁成就 |
| `POST` | `/api/v1/mints` | 手动触发链上 NFT（测试） |

---

---

## Quick Start｜快速启动

### 环境要求

- Java 17+
- Node.js 18+
- Docker / Docker Compose

### 启动步骤

```bash
# 1️⃣ 克隆项目
git clone https://github.com/yourusername/affirmation-counter.git
cd affirmation-counter

# 2️⃣ 构建后端
cd backend
./mvnw clean package -DskipTests

# 3️⃣ 构建前端
cd ../frontend
npm install
npm run build

# 4️⃣ 一键运行
cd ..
docker compose up -d

# 5️⃣ 访问
# Frontend: http://localhost:5173
# Backend:  http://localhost:8080/api/v1/health

```


## Author

**Skelita Lynn**

- 🎓 SCUT · 软件工程
- 💡 方向：后端开发 · Web3 · 分布式系统
- 📫 [skelitalynn@gmail.com](mailto:skelitalynn@gmail.com)
