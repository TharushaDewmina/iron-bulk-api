package com.tharusha.ironbulk.dto;

import java.time.LocalDate;
import java.util.List;

public record DailyNutritionSummary(
    LocalDate date,
    Double totalCalories,
    Integer targetCalories,
    Double remainingCalories,
    Double totalProteinG,
    Double targetProteinG,
    Double remainingProteinG,
    Double totalCarbsG,
    Double totalFatsG,
    List<MealLogResponse> meals
) {}