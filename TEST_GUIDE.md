# 后端项目测试指南

本指南将帮助你快速验证后端项目是否能正常运行。

## 📋 测试步骤

### 第一步：启动基础设施（MySQL 和 Redis）

后端项目依赖 MySQL 和 Redis，需要先启动它们：

```bash
cd docker
docker-compose up -d
```

验证服务是否启动成功：
```bash
docker ps
```
应该能看到 `affirm-mysql` 和 `affirm-redis` 两个容器在运行。

---

### 第二步：启动后端应用

```bash
cd affirm-backend
mvn spring-boot:run
```

或者如果你已经编译过：
```bash
mvn clean install
mvn spring-boot:run
```

启动成功后，你应该看到类似这样的日志：
```
Started AffirmApplication in X.XXX seconds
```

---

### 第三步：使用 Postman 测试接口

#### 🟢 测试 1：健康检查（最简单）

**接口**：`GET http://localhost:8080/ping`

**操作步骤**：
1. 打开 Postman
2. 选择 GET 方法
3. 输入 URL：`http://localhost:8080/ping`
4. 点击 Send

**预期结果**：
- 状态码：`200 OK`
- 响应体：
```json
{
  "status": "ok",
  "service": "affirm-backend"
}
```

✅ **如果成功，说明后端应用已启动并能接收请求！**

---

#### 🟢 测试 2：用户注册（无需认证）

**接口**：`POST http://localhost:8080/auth/register`

**操作步骤**：
1. 选择 POST 方法
2. URL：`http://localhost:8080/auth/register`
3. 点击 **Body** 标签页
4. 选择 **raw**，然后选择 **JSON** 格式
5. 输入请求体：
```json
{
  "username": "testuser",
  "password": "Test123456!"
}
```
6. 点击 Send

**预期结果**：
- 状态码：`201 Created`
- 响应体包含用户信息（id, username, createdAt）

✅ **如果成功，说明注册功能正常，数据库连接正常！**

---

#### 🟢 测试 3：用户登录（获取 JWT Token）

**接口**：`POST http://localhost:8080/sessions`

**操作步骤**：
1. 选择 POST 方法
2. URL：`http://localhost:8080/sessions`
3. Body 选择 raw + JSON：
```json
{
  "username": "testuser",
  "password": "Test123456!"
}
```
4. 点击 Send

**预期结果**：
- 状态码：`201 Created`
- 响应体：
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 7200
}
```

⚠️ **重要**：复制返回的 `token` 值，后面测试需要用到！

✅ **如果成功，说明登录和 JWT 生成功能正常！**

---

#### 🟢 测试 4：需要认证的接口（使用 Token）

**接口**：`GET http://localhost:8080/counter`

**操作步骤**：
1. 选择 GET 方法
2. URL：`http://localhost:8080/counter`
3. 点击 **Authorization** 标签页
4. 类型选择 **Bearer Token**
5. 在 Token 输入框中粘贴从测试 3 获取的 token
6. 点击 Send

**预期结果**：
- 状态码：`200 OK`
- 响应体：
```json
{
  "total": 0,
  "nextMilestone": 5000
}
```

✅ **如果成功，说明 JWT 认证机制工作正常！**

---

## 🎯 快速验证清单

完成以下测试即可确认后端运行正常：

- [ ] **基础连通性**：`GET /ping` 返回 200
- [ ] **数据库连接**：`POST /auth/register` 注册成功
- [ ] **登录功能**：`POST /sessions` 返回 token
- [ ] **JWT 认证**：`GET /counter` 使用 token 成功访问

---

## ❌ 常见问题排查

### 1. 连接数据库失败

**错误信息**：`Communications link failure` 或 `Access denied`

**解决方案**：
- 检查 MySQL 是否启动：`docker ps`
- 检查配置：`application.yml` 中的数据库连接信息
- 确认 MySQL 容器正常运行：
  ```bash
  docker logs affirm-mysql
  ```

### 2. Redis 连接失败

**错误信息**：`Unable to connect to Redis`

**解决方案**：
- 检查 Redis 是否启动：`docker ps`
- 检查端口是否被占用：`netstat -an | findstr 6379`（Windows）

### 3. 端口 8080 被占用

**错误信息**：`Port 8080 is already in use`

**解决方案**：
- 修改 `application.yml` 中的 `server.port`
- 或者关闭占用 8080 端口的程序

### 4. 401 Unauthorized（认证失败）

**错误信息**：`401 Unauthorized` 或 `Invalid JWT`

**解决方案**：
- 确认在 Authorization 中设置了 Bearer Token
- 确认 token 是从 `/sessions` 接口获取的最新 token
- 检查 token 是否已过期（默认 7200 秒，2 小时）

---

## 📝 Postman 配置建议

### 创建环境变量（方便测试）

1. 点击右上角的 **Environment** 图标
2. 创建新环境，添加变量：
   - `baseUrl`: `http://localhost:8080`
   - `token`: （登录后手动更新）
3. 在 URL 中使用：`{{baseUrl}}/ping`
4. 在 Authorization 中使用：`{{token}}`

### 创建 Collection（组织测试用例）

1. 点击 **New** → **Collection**
2. 命名为 "Affirm Backend Tests"
3. 将上述测试接口都添加到这个 Collection 中
4. 方便后续重复测试和分享

---

## 🚀 进阶测试

如果基础测试都通过了，可以继续测试其他接口：

1. **断言管理**：
   - `GET /affirm` - 获取断言列表
   - `POST /affirm` - 创建断言
   - `DELETE /affirm/{id}` - 删除断言

2. **计数器**：
   - `PATCH /counter` - 自增计数器

3. **成就系统**：
   - `GET /achi` - 获取成就列表
   - `POST /achi/{id}/mint` - 铸造 NFT

**注意**：所有上述接口都需要 JWT token 认证！

---

祝你测试顺利！🎉

