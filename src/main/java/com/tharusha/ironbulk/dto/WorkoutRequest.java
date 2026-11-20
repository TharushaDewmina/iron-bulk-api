package com.tharusha.ironbulk.dto;

import com.tharusha.ironbulk.entity.SplitType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record WorkoutRequest(
    @NotNull SplitType splitType,
    @NotNull LocalDate workoutDate,
    String notes,
    @NotEmpty @Valid List<WorkoutSetRequest> sets
) {}