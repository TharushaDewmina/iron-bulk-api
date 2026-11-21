package com.tharusha.ironbulk.controller;

import com.tharusha.ironbulk.dto.DailyNutritionSummary;
import com.tharusha.ironbulk.dto.MealLogRequest;
import com.tharusha.ironbulk.dto.MealLogResponse;
import com.tharusha.ironbulk.service.MealLogService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/meals")
public class MealLogController {

    private final MealLogService mealLogService;

    public MealLogController(MealLogService mealLogService) {
        this.mealLogService = mealLogService;
    }

    @PostMapping
    public ResponseEntity<MealLogResponse> logMeal(
            Authentication authentication,
            @Valid @RequestBody MealLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mealLogService.logMeal(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<DailyNutritionSummary> getDailySummary(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(mealLogService.getDailySummary(authentication.getName(), date));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(
            Authentication authentication,
            @PathVariable Long id) {
        mealLogService.deleteMeal(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}