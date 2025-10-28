package com.affirm.controller;

import com.affirm.dto.counter.CounterIncrementRequest;
import com.affirm.dto.counter.CounterResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counter")
public class CounterController {

    @GetMapping
    public ResponseEntity<CounterResponse> getMyCounter() {
        CounterResponse resp = new CounterResponse();
        resp.setTotal(0L);
        resp.setNextMilestone(5000L);
        return ResponseEntity.ok(resp);
    }

    @PatchMapping
    public ResponseEntity<CounterResponse> increment(@Valid @RequestBody CounterIncrementRequest request) {
        CounterResponse resp = new CounterResponse();
        resp.setTotal(1L);
        resp.setCrossedMilestone(false);
        resp.setNextMilestone(5000L);
        return ResponseEntity.ok(resp);
    }
}


