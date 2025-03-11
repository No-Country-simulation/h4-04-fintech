package com.practice.Django.Recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRecommendationsResponse {
    private Long userId;
    private List<Recommendation> recommendations;
}
