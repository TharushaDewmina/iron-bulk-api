package com.tharusha.ironbulk.dto;

import java.time.LocalDate;

public record BodyWeightResponse(
    Long id,
    LocalDate logDate,
    Double weightKg,
    String notes
) {}