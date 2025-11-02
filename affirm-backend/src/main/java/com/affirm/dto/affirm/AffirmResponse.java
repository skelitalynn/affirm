package com.affirm.dto.affirm;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class AffirmResponse {
    private Long id;
    private String text;
    private OffsetDateTime createdAt;
}




