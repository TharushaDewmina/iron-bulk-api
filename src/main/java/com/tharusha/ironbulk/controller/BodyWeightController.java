package com.tharusha.ironbulk.controller;

import com.tharusha.ironbulk.dto.BodyWeightRequest;
import com.tharusha.ironbulk.dto.BodyWeightResponse;
import com.tharusha.ironbulk.service.BodyWeightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weights")
public class BodyWeightController {

    private final BodyWeightService bodyWeightService;

    public BodyWeightController(BodyWeightService bodyWeightService) {
        this.bodyWeightService = bodyWeightService;
    }

    @PostMapping
    public ResponseEntity<BodyWeightResponse> upsertWeight(
            Authentication authentication,
            @Valid @RequestBody BodyWeightRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bodyWeightService.upsertWeight(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<BodyWeightResponse>> getWeightHistory(Authentication authentication) {
        return ResponseEntity.ok(bodyWeightService.getWeightHistory(authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeight(
            Authentication authentication,
            @PathVariable Long id) {
        bodyWeightService.deleteWeight(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}