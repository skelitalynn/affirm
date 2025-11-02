package com.affirm.controller;

import com.affirm.domain.User;
import com.affirm.dto.auth.LoginRequest;
import com.affirm.dto.auth.RegisterRequest;
import com.affirm.dto.auth.SessionResponse;
import com.affirm.service.AuthService;
import com.affirm.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    /**
     * 用户注册接口
     * POST /auth/register
     * 
     * 流程：
     * 1. 验证用户名是否已存在
     * 2. 使用BCrypt加密密码
     * 3. 创建用户并初始化计数器
     * 4. 返回用户信息（不含token，需要登录获取）
     */
    @PostMapping("/auth/register")
    public ResponseEntity<SessionResponse.UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request.getUsername(), request.getPassword());
        SessionResponse.UserResponse body = new SessionResponse.UserResponse();
        body.setId(user.getId());
        body.setUsername(user.getUsername());
        body.setCreatedAt(java.time.OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    /**
     * 用户登录接口（创建会话）
     * POST /sessions
     * 
     * 流程：
     * 1. 验证用户名和密码
     * 2. 生成JWT token（包含用户名、签发时间、过期时间）
     * 3. 返回token和过期时间（单位：秒）
     * 
     * 客户端需要在后续请求的Header中携带：
     * Authorization: Bearer <token>
     */
    @PostMapping("/sessions")
    public ResponseEntity<SessionResponse> createSession(@Valid @RequestBody LoginRequest request) {
        // 验证用户名和密码
        User user = authService.authenticate(request.getUsername(), request.getPassword());
        // 生成JWT token（subject设置为用户名）
        String token = jwtService.generateToken(user.getUsername());
        // 构建响应
        SessionResponse body = new SessionResponse();
        body.setToken(token);
        body.setExpiresIn((int) jwtService.getExpiresSeconds());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    /**
     * 退出登录接口（删除会话）
     * DELETE /sessions/me
     * 
     * 注意：由于JWT是无状态的，服务端无法主动使token失效
     * 当前实现：仅返回204，客户端需要删除本地存储的token
     * 
     * 如果需要真正的token失效功能，可以：
     * 1. 使用Redis存储token黑名单（token id + 过期时间）
     * 2. 在SecurityConfig中添加过滤器检查黑名单
     * 3. 退出时将token加入黑名单
     */
    @DeleteMapping("/sessions/me")
    public ResponseEntity<Void> deleteSession() {
        // TODO: 如果需要真正的token失效，可以在这里将token加入Redis黑名单
        // 当前实现：仅返回成功，客户端需要自行删除token
        return ResponseEntity.noContent().build();
    }
}



