# RESTful API

## 1. 认证与会话

### 1.1 注册
- `POST /auth/register`
- 请求
```json
{
  "username": "lynn",
  "password": "P@ssw0rd!"
}
```
- 响应 `201 Created`
```json
{
  "id": 123,
  "username": "lynn",
  "createdAt": "2025-10-27T08:00:00Z"
}
```

### 1.2 登录（创建会话）
- `POST /sessions`
- 请求
```json
{
  "username": "lynn",
  "password": "P@ssw0rd!"
}
```
- 响应 `201 Created`
```json
{
  "token": "eyJhbGci...",
  "expiresIn": 7200
}
```

### 1.3 退出登录（删除会话）
- `DELETE /sessions/me`
- 响应 `204 No Content`

> 说明：登录/登出是“动作型”接口，采用会话资源（`/sessions`）的 RESTful 表达。

---

## 2. 肯定语（Affirmations）

### 2.1 列表
- `GET /affirm`
- 响应 `200 OK`
```json
[
  { "id": 1, "text": "I am focused and calm.", "createdAt": "2025-10-01T10:00:00Z" }
]
```

### 2.2 新增（最多 5 条）
- `POST /affirm`
- 请求
```json
{ "text": "I am a top-tier engineer." }
```
- 响应 `201 Created`
```json
{ "id": 2, "text": "I am a top-tier engineer.", "createdAt": "2025-10-27T08:00:00Z" }
```
- 约束：超出 5 条返回 `409 Conflict`

### 2.3 删除
- `DELETE /affirm/{id}` → `204 No Content`

---

## 3. 计数（Counter）

### 3.1 查询我的累计次数
- `GET /counter`
- 响应
```json
{
  "total": 1234,
  "nextMilestone": 5000
}
```

### 3.2 自增（更语义化的 REST 写法：PATCH）
- `PATCH /counter`
- 请求（增量更新语义）
```json
{ "increment": 1 }
```
- 响应
```json
{
  "total": 1235,
  "crossedMilestone": false,
  "nextMilestone": 5000
}
```

> 说明：`PATCH /counter` 表示对资源进行“部分更新”，`increment` 字段表达自增步长（默认 1）。

---

## 4. 成就（Achi）

### 4.1 列表我的成就
- `GET /achi`
- 响应
```json
[
  { "id": 10, "milestone": 5000, "achievedAt": "2025-10-20T12:30:00Z", "minted": true, "tokenId": "101" }
]
```

### 4.2 为指定成就铸造 NFT
- `POST /achi/{id}/mint`
- 响应
```json
{
  "jobId": 77,
  "status": "PENDING",
  "txHash": null
}
```

---

## 5. 铸造任务（Mint Jobs, 可选查询）

### 5.1 查询任务
- `GET /mints/{jobId}`
- 响应
```json
{
  "jobId": 77,
  "achievementId": 10,
  "status": "CONFIRMED",
  "txHash": "0xabc...",
  "updatedAt": "2025-10-27T08:01:00Z"
}
```

---

## 6. 错误规范（RFC 7807 — Problem+JSON）

统一返回：`application/problem+json`
```json
{
  "type": "https://api.example.com/errors/rate-limit",
  "title": "Too many requests",
  "status": 429,
  "detail": "Rate limit 60 req/min exceeded",
  "traceId": "f31f..."
}
```

常见错误：
- 400 参数错误；401 未认证；403 无权限；404 不存在；409 资源冲突；429 频率限制；500 服务器故障。

---

## 7. 安全、幂等与限流

- **认证**：HTTP Header `Authorization: Bearer <JWT>`
- **幂等**：对敏感写操作（如 `/achi/{id}/mint`）支持 `Idempotency-Key`，服务端使用幂等表去重。
- **限流**：滑动窗口，维度 = IP + userId。超限返回 429。
- **可缓存**：`GET` 列表/详情可设置 `ETag/Last-Modified`（可选）。

---

## 8. 分页与过滤（通用约定）

- 分页参数：`?page=1&size=20`
- 响应头：`X-Total-Count`
- 过滤：`/affirm?keyword=calm`
- 排序：`?sort=createdAt,desc`