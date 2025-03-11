package com.practice.Objectives.dtoResponse;

import java.time.LocalDateTime;

public record ObjectivesResponseCreateDto(
        Long id,
        String description,
        String objectiveType,
        Double amountObjective,
        Double annualProgress,
        String priority,
        String frequency,
//        Long idFinancingProfile,
        LocalDateTime startDate,
        LocalDateTime targetDate
) {
}
