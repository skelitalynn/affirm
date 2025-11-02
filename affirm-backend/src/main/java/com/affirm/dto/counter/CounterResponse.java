package com.affirm.dto.counter;

import lombok.Data;

@Data
public class CounterResponse {
    private Long total;
    private Boolean crossedMilestone;
    private Long nextMilestone;
}






