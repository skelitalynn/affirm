package com.affirm.controller;

import com.affirm.dto.counter.CounterIncrementRequest;
import com.affirm.dto.counter.CounterResponse;
import com.affirm.service.CounterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/counter")
public class CounterController {

    private final CounterService counterService;

    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @GetMapping
    public ResponseEntity<CounterResponse> getMyCounter(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        long total = counterService.getTotalByUsername(username);
        CounterResponse resp = new CounterResponse();
        resp.setTotal(total);
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



