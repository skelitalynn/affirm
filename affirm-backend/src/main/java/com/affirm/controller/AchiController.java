package com.affirm.controller;

import com.affirm.dto.achi.AchievementResponse;
import com.affirm.dto.achi.MintJobResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class AchiController {

    @GetMapping("/achi")
    public ResponseEntity<List<AchievementResponse>> listMyAchievements() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping("/achi/{id}/mint")
    public ResponseEntity<MintJobResponse> mint(@PathVariable("id") Long id) {
        MintJobResponse resp = new MintJobResponse();
        resp.setJobId(77L);
        resp.setStatus("PENDING");
        resp.setTxHash(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/mints/{jobId}")
    public ResponseEntity<MintJobResponse> getMintJob(@PathVariable("jobId") Long jobId) {
        MintJobResponse resp = new MintJobResponse();
        resp.setJobId(jobId);
        resp.setStatus("CONFIRMED");
        resp.setTxHash("0xabc...");
        return ResponseEntity.ok(resp);
    }
}


