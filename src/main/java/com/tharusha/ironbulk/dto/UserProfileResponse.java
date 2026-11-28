package com.tharusha.ironbulk.dto;

import java.time.LocalDateTime;

public record UserProfileResponse(
    Long id,
    String username,
    String email,
    Integer dailyCalorieTarget,
    Double dailyProteinTarget,
    LocalDateTime createdAt
) {}