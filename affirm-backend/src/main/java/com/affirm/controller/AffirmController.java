package com.affirm.controller;

import com.affirm.dto.affirm.AffirmCreateRequest;
import com.affirm.dto.affirm.AffirmResponse;
import com.affirm.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/affirm")
public class AffirmController {

    /**
     * 获取当前用户的肯定语列表
     * 通过@AuthenticationPrincipal获取JWT token，从中提取用户名
     */
    @GetMapping
    public ResponseEntity<List<AffirmResponse>> list(
            @AuthenticationPrincipal Jwt jwt) {
        // 从JWT token中获取用户名
        String username = jwt.getSubject();
        // TODO: 根据username查询该用户的肯定语列表
        // List<AffirmResponse> items = affirmService.findByUsername(username);
        List<AffirmResponse> items = new ArrayList<>();
        return ResponseEntity.ok(items);
    }

    /**
     * 创建新的肯定语
     * 使用SecurityUtils工具类获取当前用户名（两种方式都可以）
     */
    @PostMapping
    public ResponseEntity<AffirmResponse> create(
            @Valid @RequestBody AffirmCreateRequest request) {
        // 方式1：使用@AuthenticationPrincipal参数（推荐，更明确）
        // 方式2：使用SecurityUtils工具类（在Service层使用更方便）
        String username = SecurityUtils.getCurrentUsername();
        
        // TODO: 根据username保存肯定语，关联到当前用户
        // AffirmResponse body = affirmService.create(request.getText(), username);
        AffirmResponse body = new AffirmResponse();
        body.setId(1L);
        body.setText(request.getText());
        body.setCreatedAt(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    /**
     * 删除肯定语
     * 需要验证该肯定语属于当前用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal Jwt jwt) {
        // 从JWT token中获取当前用户名
        String username = jwt.getSubject();
        // TODO: 验证该id的肯定语是否属于当前用户，然后删除
        // affirmService.deleteByIdAndUsername(id, username);
        return ResponseEntity.noContent().build();
    }
}




