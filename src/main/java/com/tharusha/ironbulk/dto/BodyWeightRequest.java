package com.tharusha.ironbulk.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record BodyWeightRequest(
    @NotNull LocalDate logDate,
    @NotNull @Positive @DecimalMax("350.0") Double weightKg,
    @Size(max = 200) String notes
) {}