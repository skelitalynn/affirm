package com.affirm.dto.affirm;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AffirmCreateRequest {
    @NotBlank
    private String text;
}




