package com.tharusha.ironbulk.controller;

import com.tharusha.ironbulk.dto.WorkoutRequest;
import com.tharusha.ironbulk.dto.WorkoutResponse;
import com.tharusha.ironbulk.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<WorkoutResponse> logWorkout(
            Authentication authentication,
            @Valid @RequestBody WorkoutRequest request) {
        WorkoutResponse response = workoutService.logWorkout(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkoutResponse>> getWorkouts(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(workoutService.getUserWorkouts(authentication.getName(), date));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(
            Authentication authentication,
            @PathVariable Long id) {
        workoutService.deleteWorkout(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}