package com.practice.Objectives.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectivesResponseDto {
    private Long id;
    private String description;
    private String objectiveType;
    private Double amountObjective;
    private Double annualProgress;
    private String priority;
    private String frequency;
//    private ObjectiveFinancingProfileInfoDto financingProfile;
}
