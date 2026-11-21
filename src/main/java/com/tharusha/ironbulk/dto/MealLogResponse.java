package com.tharusha.ironbulk.dto;

import java.time.LocalDate;

public record MealLogResponse(
    Long id,
    LocalDate logDate,
    String foodName,
    Double grams,
    Double calories,
    Double proteinG,
    Double carbsG,
    Double fatsG
) {}