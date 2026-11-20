package com.tharusha.ironbulk.dto;

import jakarta.validation.constraints.*;

public record WorkoutSetRequest(
    @NotBlank String exerciseName,
    @NotNull @Min(1) Integer setNumber,
    @NotNull @PositiveOrZero Double weightKg,
    @NotNull @Min(1) Integer reps,
    Boolean isDropSet
) {}