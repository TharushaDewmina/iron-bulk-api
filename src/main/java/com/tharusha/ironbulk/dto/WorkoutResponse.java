package com.tharusha.ironbulk.dto;

import com.tharusha.ironbulk.entity.SplitType;
import java.time.LocalDate;
import java.util.List;

public record WorkoutResponse(
    Long id,
    SplitType splitType,
    LocalDate workoutDate,
    String notes,
    Double totalVolumeKg,
    List<WorkoutSetResponse> sets
) {}