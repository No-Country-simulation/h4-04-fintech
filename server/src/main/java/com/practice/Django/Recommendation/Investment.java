package com.practice.Django.Recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Investment {
    @JsonProperty("investment_id")
    private Long investmentId;

    @JsonProperty("investment_type")
    private String investmentType;

    @JsonProperty("risk_level")
    private Integer riskLevel;

    @JsonProperty("min_investment")
    private String minInvestment;

    @JsonProperty("expected_return")
    private String expectedReturn;
}
