package com.practice.Objectives.dtoRequest;

import java.time.LocalDateTime;

public record ObjectivesCreateRequestDto(
         Long id ,
         String description ,
         String objectiveType ,
         Double amountObjective ,
         Double annualProgress ,
         String priority ,
         String frequency,
//         Long  idFinancingProfile
         LocalDateTime startDate,
         LocalDateTime targetDate
) {
}
