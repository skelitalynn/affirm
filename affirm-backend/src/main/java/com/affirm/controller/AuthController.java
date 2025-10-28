package com.affirm.controller;

import com.affirm.dto.auth.LoginRequest;
import com.affirm.dto.auth.RegisterRequest;
import com.affirm.dto.auth.SessionResponse;
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

    @PostMapping("/auth/register")
    public ResponseEntity<SessionResponse.UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        SessionResponse.UserResponse body = new SessionResponse.UserResponse();
        body.setId(1L);
        body.setUsername(request.getUsername());
        body.setCreatedAt(java.time.OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/sessions")
    public ResponseEntity<SessionResponse> createSession(@Valid @RequestBody LoginRequest request) {
        SessionResponse body = new SessionResponse();
        body.setToken("demo-token");
        body.setExpiresIn(7200);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/sessions/me")
    public ResponseEntity<Void> deleteSession() {
        return ResponseEntity.noContent().build();
    }
}


