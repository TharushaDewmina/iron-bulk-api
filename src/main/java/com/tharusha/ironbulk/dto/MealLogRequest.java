package com.tharusha.ironbulk.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record MealLogRequest(
    @NotNull LocalDate logDate,
    @NotBlank @Size(max = 100) String foodName,
    @NotNull @Positive Double grams,
    @NotNull @PositiveOrZero Double calories,
    @NotNull @PositiveOrZero Double proteinG,
    @NotNull @PositiveOrZero Double carbsG,
    @NotNull @PositiveOrZero Double fatsG
) {}