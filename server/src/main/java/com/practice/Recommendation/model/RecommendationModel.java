package com.practice.Recommendation.model;

import com.practice.Instrument.Enum.EnumInvestmentType;
import com.practice.Instrument.model.InstrumentModel;
import com.practice.Recommendation.Enum.EnumRecommendationType;
import com.practice.User.model.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "recommendations")
public class RecommendationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "recommendation_type")
    @Enumerated(EnumType.STRING)
    EnumRecommendationType enumRecommendationType;
    private String motion;
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();
    private Boolean state;

    @ManyToOne(targetEntity = UserModel.class)
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne(targetEntity = InstrumentModel.class)
    @JoinColumn(name = "instrument_id")
    private InstrumentModel instrument;
}
