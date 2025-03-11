package com.practice.Django;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInvestmentResponse {
    private String status;
    private Long investmentId;
    private Double amount;
    private String investmentType;
    private Double expectedReturn;
    @JsonProperty("new_cluster")
    private String newCluster;
}
