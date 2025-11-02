# 登录功能实现教程

## 📋 目录
1. [登录功能架构概览](#登录功能架构概览)
2. [核心组件说明](#核心组件说明)
3. [完整登录流程](#完整登录流程)
4. [如何在代码中使用](#如何在代码中使用)
5. [测试登录功能](#测试登录功能)

---

## 登录功能架构概览

### 技术栈
- **Spring Security**: 安全框架
- **JWT (JSON Web Token)**: 无状态认证
- **BCrypt**: 密码加密
- **Spring OAuth2 Resource Server**: JWT验证

### 核心文件结构
```
affirm-backend/
├── controller/
│   └── AuthController.java          # 登录、注册、退出接口
├── service/
│   ├── AuthService.java              # 认证逻辑（用户名密码验证）
│   └── JwtService.java               # JWT token生成
├── config/
│   └── SecurityConfig.java           # Spring Security配置
├── util/
│   └── SecurityUtils.java           # 获取当前用户的工具类
└── dto/auth/
    ├── LoginRequest.java             # 登录请求DTO
    └── SessionResponse.java          # 登录响应DTO
```

---

## 核心组件说明

### 1. SecurityConfig - 安全配置中心

**位置**: `config/SecurityConfig.java`

**核心功能**:
- 配置哪些接口需要认证，哪些公开访问
- 配置JWT验证机制
- 配置密码加密器

**关键配置**:
```java
// 公开接口（无需token）
.requestMatchers("/auth/register", "/sessions", "/sessions/me").permitAll()

// 其他接口都需要认证（需要有效的JWT token）
.anyRequest().authenticated()

// 配置JWT资源服务器（自动验证Bearer token）
.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
```

### 2. AuthController - 认证控制器

**位置**: `controller/AuthController.java`

**三个核心接口**:

#### ① 注册接口 `POST /auth/register`
- 创建新用户
- 密码自动BCrypt加密
- 初始化用户计数器

#### ② 登录接口 `POST /sessions`
- 验证用户名密码
- 生成JWT token
- 返回token和过期时间

#### ③ 退出接口 `DELETE /sessions/me`
- 当前实现：仅返回成功
- 客户端需要自行删除本地token

### 3. AuthService - 认证服务

**位置**: `service/AuthService.java`

**核心方法**:

```java
// 用户注册
public User register(String username, String rawPassword)

// 用户认证（验证用户名密码）
public User authenticate(String username, String rawPassword)
```

**安全特性**:
- 密码使用BCrypt加密存储
- 登录时使用BCrypt验证密码
- 用户名唯一性校验

### 4. JwtService - JWT服务

**位置**: `service/JwtService.java`

**核心方法**:
```java
// 生成JWT token
public String generateToken(String subject)
```

**Token包含信息**:
- `subject`: 用户名
- `issuedAt`: 签发时间
- `expiration`: 过期时间（默认2小时）

**配置**:
- 密钥: `app.jwt.secret` (application.yml)
- 过期时间: `app.jwt.expires-seconds` (默认7200秒)

### 5. SecurityUtils - 安全工具类

**位置**: `util/SecurityUtils.java`

**实用方法**:
```java
// 获取当前登录用户名
SecurityUtils.getCurrentUsername()

// 获取当前JWT对象
SecurityUtils.getCurrentJwt()

// 检查是否已登录
SecurityUtils.isAuthenticated()
```

---

## 完整登录流程

### 步骤1: 用户注册
```
客户端 → POST /auth/register
请求体: {"username": "lynn", "password": "P@ssw0rd!"}

服务端处理:
1. 检查用户名是否已存在
2. 使用BCrypt加密密码
3. 保存用户到数据库
4. 初始化用户计数器
5. 返回用户信息（不含token）
```

### 步骤2: 用户登录
```
客户端 → POST /sessions
请求体: {"username": "lynn", "password": "P@ssw0rd!"}

服务端处理:
1. 从数据库查询用户
2. 使用BCrypt验证密码
3. 验证通过后生成JWT token
4. 返回token和过期时间

响应:
{
  "token": "eyJhbGci...",
  "expiresIn": 7200
}
```

### 步骤3: 使用Token访问受保护接口
```
客户端 → GET /counter
请求头: Authorization: Bearer eyJhbGci...

服务端处理:
1. Spring Security自动提取Bearer token
2. 使用JwtDecoder验证token（签名、过期时间）
3. 验证通过后设置Authentication对象
4. 执行Controller方法
```

### 步骤4: 在Controller中获取当前用户
```java
@GetMapping("/counter")
public ResponseEntity<CounterResponse> getMyCounter(
    @AuthenticationPrincipal Jwt jwt) {
    String username = jwt.getSubject(); // 获取用户名
    // 根据username查询数据
}
```

---

## 如何在代码中使用

### 方式1: 在Controller中使用 @AuthenticationPrincipal（推荐）

```java
@RestController
@RequestMapping("/affirm")
public class AffirmController {
    
    @GetMapping
    public ResponseEntity<List<AffirmResponse>> list(
            @AuthenticationPrincipal Jwt jwt) {
        // 从JWT token中获取用户名
        String username = jwt.getSubject();
        
        // 根据username查询该用户的数据
        // ...
    }
}
```

**优点**:
- 类型安全
- 明确表明该接口需要认证
- IDE自动提示

### 方式2: 使用SecurityUtils工具类（适用于Service层）

```java
@Service
public class AffirmService {
    
    public void createAffirmation(String text) {
        // 在Service层获取当前用户名
        String username = SecurityUtils.getCurrentUsername();
        
        if (username == null) {
            throw new IllegalStateException("用户未登录");
        }
        
        // 根据username保存数据
        // ...
    }
}
```

**优点**:
- Service层不需要依赖Spring Security的Jwt类
- 代码更简洁
- 便于单元测试（可mock SecurityContext）

### 方式3: 从SecurityContext直接获取

```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
    Jwt jwt = (Jwt) authentication.getPrincipal();
    String username = jwt.getSubject();
}
```

---

## 测试登录功能

### 1. 使用curl测试注册

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"lynn","password":"P@ssw0rd!"}'
```

**预期响应** (201 Created):
```json
{
  "id": 123,
  "username": "lynn",
  "createdAt": "2025-10-27T08:00:00Z"
}
```

### 2. 使用curl测试登录

```bash
curl -X POST http://localhost:8080/sessions \
  -H "Content-Type: application/json" \
  -d '{"username":"lynn","password":"P@ssw0rd!"}'
```

**预期响应** (201 Created):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 7200
}
```

### 3. 使用token访问受保护接口

```bash
# 保存token到变量
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# 使用token访问接口
curl -X GET http://localhost:8080/counter \
  -H "Authorization: Bearer $TOKEN"
```

**预期响应** (200 OK):
```json
{
  "total": 1234,
  "nextMilestone": 5000
}
```

### 4. 测试未携带token访问（应该返回401）

```bash
curl -X GET http://localhost:8080/counter
```

**预期响应** (401 Unauthorized):
```json
{
  "error": "unauthorized",
  "message": "Full authentication is required to access this resource"
}
```

### 5. 测试无效token（应该返回401）

```bash
curl -X GET http://localhost:8080/counter \
  -H "Authorization: Bearer invalid_token_here"
```

---

## 🔐 安全要点

### ✅ 已实现的安全措施

1. **密码加密**: 使用BCrypt，不会明文存储密码
2. **JWT签名**: 使用HMAC-SHA256，防止token被篡改
3. **Token过期**: 默认2小时，防止token永久有效
4. **无状态认证**: 使用JWT，服务端无需存储session

### ⚠️ 需要注意的问题

1. **Token失效**: 当前退出登录不会真正使token失效（JWT特性）
   - 解决：可以使用Redis黑名单机制
   
2. **密钥安全**: `app.jwt.secret` 应该使用强密钥
   - 生产环境应该从环境变量或密钥管理服务读取
   
3. **HTTPS**: 生产环境必须使用HTTPS，防止token被中间人攻击

---

## 📝 后续改进建议

1. **Token刷新机制**: 实现refresh token，避免用户频繁登录
2. **登录失败限制**: 防止暴力破解，限制登录尝试次数
3. **多设备登录管理**: 记录登录设备，支持强制下线
4. **Token黑名单**: 使用Redis实现真正的token失效功能

---

## 🎯 总结

你的登录功能已经完整实现，包括：

✅ 用户注册（密码加密）  
✅ 用户登录（JWT token生成）  
✅ 受保护接口认证（自动验证token）  
✅ 获取当前用户信息（多种方式）  
✅ 安全配置（Spring Security + JWT）

现在你可以在任何Controller或Service中轻松获取当前登录用户，实现用户相关的业务逻辑！

