package com.tharusha.ironbulk.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
    @NotBlank @Size(min = 3, max = 50) String username,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 6, max = 100) String password,
    @Min(1200) @Max(6000) Integer dailyCalorieTarget,
    @Min(50) @Max(400) Double dailyProteinTarget
) {}