package com.tharusha.ironbulk.dto;

public record WorkoutSetResponse(
    Long id,
    String exerciseName,
    Integer setNumber,
    Double weightKg,
    Integer reps,
    Boolean isDropSet
) {}