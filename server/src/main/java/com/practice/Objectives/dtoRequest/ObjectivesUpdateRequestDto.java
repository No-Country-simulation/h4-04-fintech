package com.practice.Objectives.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectivesUpdateRequestDto {
    private Long id;
    private String description;
    private String objectiveType;
    private Double amountObjective;
    private Double annualProgress;
    private String priority;
    private String frequency;
    private LocalDateTime startDate = LocalDateTime.now();
    private LocalDateTime targetDate = LocalDateTime.now();
//    private Long idFinancingProfile;

}
