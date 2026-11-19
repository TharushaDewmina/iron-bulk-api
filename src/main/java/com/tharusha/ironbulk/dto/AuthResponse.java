package com.tharusha.ironbulk.dto;

public record AuthResponse(
    String token,
    String username,
    Integer dailyCalorieTarget,
    Double dailyProteinTarget
) {}