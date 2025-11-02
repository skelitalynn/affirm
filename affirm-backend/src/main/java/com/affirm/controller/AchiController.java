package com.affirm.controller;

import com.affirm.dto.achi.AchievementResponse;
import com.affirm.dto.achi.MintJobResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class AchiController {

    /**
     * 获取当前用户的成就列表
     */
    @GetMapping("/achi")
    public ResponseEntity<List<AchievementResponse>> listMyAchievements(
            @AuthenticationPrincipal Jwt jwt) {
        // 从JWT token中获取当前用户名
        String username = jwt.getSubject();
        // TODO: 根据username查询该用户的成就列表
        // List<AchievementResponse> achievements = achievementService.findByUsername(username);
        return ResponseEntity.ok(new ArrayList<>());
    }

    /**
     * 为指定成就铸造NFT
     * 需要验证该成就属于当前用户
     */
    @PostMapping("/achi/{id}/mint")
    public ResponseEntity<MintJobResponse> mint(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal Jwt jwt) {
        // 从JWT token中获取当前用户名
        String username = jwt.getSubject();
        // TODO: 验证该成就属于当前用户，然后创建铸造任务
        // achievementService.validateOwnership(id, username);
        MintJobResponse resp = new MintJobResponse();
        resp.setJobId(77L);
        resp.setStatus("PENDING");
        resp.setTxHash(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    /**
     * 查询铸造任务状态
     * 需要验证该任务属于当前用户
     */
    @GetMapping("/mints/{jobId}")
    public ResponseEntity<MintJobResponse> getMintJob(
            @PathVariable("jobId") Long jobId,
            @AuthenticationPrincipal Jwt jwt) {
        // 从JWT token中获取当前用户名
        String username = jwt.getSubject();
        // TODO: 验证该jobId属于当前用户，然后查询任务状态
        // mintService.validateOwnership(jobId, username);
        MintJobResponse resp = new MintJobResponse();
        resp.setJobId(jobId);
        resp.setStatus("CONFIRMED");
        resp.setTxHash("0xabc...");
        return ResponseEntity.ok(resp);
    }
}




