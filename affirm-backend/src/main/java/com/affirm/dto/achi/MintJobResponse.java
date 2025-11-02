package com.affirm.dto.achi;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MintJobResponse {
    private Long jobId;
    private Long achievementId;
    private String status;
    private String txHash;
    private OffsetDateTime updatedAt;
}






