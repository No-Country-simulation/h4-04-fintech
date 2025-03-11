package com.practice.Django;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInvestmentRequest {
    private Long userId;
    private Long investmentId;
    private Double amount;
}
