package com.practice.Recommendation.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequestPrueba {
    private String riskProfile;
    private Double incomeMonthly;
    private Double expensesMonthly;
    private Integer percentageSave;
}
