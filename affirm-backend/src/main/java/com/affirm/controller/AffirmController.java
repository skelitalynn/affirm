package com.affirm.controller;

import com.affirm.dto.affirm.AffirmCreateRequest;
import com.affirm.dto.affirm.AffirmResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/affirm")
public class AffirmController {

    @GetMapping
    public ResponseEntity<List<AffirmResponse>> list() {
        List<AffirmResponse> items = new ArrayList<>();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<AffirmResponse> create(@Valid @RequestBody AffirmCreateRequest request) {
        AffirmResponse body = new AffirmResponse();
        body.setId(1L);
        body.setText(request.getText());
        body.setCreatedAt(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        return ResponseEntity.noContent().build();
    }
}


