package com.affirm.dto.achi;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class AchievementResponse {
    private Long id;
    private Long milestone;
    private OffsetDateTime achievedAt;
    private Boolean minted;
    private String tokenId;
}






