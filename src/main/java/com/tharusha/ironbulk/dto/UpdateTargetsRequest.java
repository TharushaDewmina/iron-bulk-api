package com.tharusha.ironbulk.dto;

import jakarta.validation.constraints.*;

public record UpdateTargetsRequest(
    @NotNull @Min(1000) @Max(10000) Integer dailyCalorieTarget,
    @NotNull @DecimalMin("30.0") @DecimalMax("500.0") Double dailyProteinTarget
) {}