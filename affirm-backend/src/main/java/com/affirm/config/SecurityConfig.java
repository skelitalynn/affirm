package com.affirm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class SecurityConfig {

    /**
     * 配置Spring Security过滤器链
     * 
     * 登录功能流程：
     * 1. 用户调用 POST /sessions 进行登录（无需token）
     * 2. 登录成功后返回JWT token
     * 3. 后续请求在Header中携带: Authorization: Bearer <token>
     * 4. Spring Security通过JwtDecoder验证token并设置Authentication
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF（因为我们使用JWT，无状态）
            .csrf(csrf -> csrf.disable())
            // 设置无状态会话（每次请求都需要携带token）
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置URL访问权限
            .authorizeHttpRequests(auth -> auth
                // 公开接口：健康检查和监控
                .requestMatchers("/ping", "/actuator/**").permitAll()
                // 公开接口：注册和登录（无需token）
                .requestMatchers("/auth/register", "/sessions", "/sessions/me").permitAll()
                // 其他所有接口都需要认证（需要有效的JWT token）
                .anyRequest().authenticated()
            )
            // 禁用HTTP Basic认证
            .httpBasic(h -> h.disable())
            // 配置OAuth2资源服务器，使用JWT进行认证
            // Spring Security会自动从Header中提取Bearer token并验证
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${app.jwt.secret}") String secret) {
        SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }
}


