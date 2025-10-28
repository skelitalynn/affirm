package com.affirm.dto.counter;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CounterIncrementRequest {
    @Min(1)
    private Integer increment = 1;
}


