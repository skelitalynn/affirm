package com.affirm.dto.auth;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SessionResponse {
    private String token;
    private Integer expiresIn;

    @Data
    public static class UserResponse {
        private Long id;
        private String username;
        private OffsetDateTime createdAt;
    }
}


