package com.practice.User.model;

import com.practice.Cost.model.CostModel;
import com.practice.FinancingProfile.model.FinancingProfileModel;
import com.practice.Notification.model.NotificationModel;
import com.practice.Portfolio.model.PortfolioModel;
import com.practice.Recommendation.model.RecommendationModel;
import com.practice.Wallet.model.WalletModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "google_id")
    private String googleId;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private Long phoneNumber;

    private String country;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "register_date")
    private LocalDateTime registerDate = LocalDateTime.now();

    @Column(name = "last_login")
    private LocalDateTime lastLogin = LocalDateTime.now();


    @OneToOne(mappedBy = "user", targetEntity = FinancingProfileModel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FinancingProfileModel financingProfile;

    @OneToMany(mappedBy = "user", targetEntity = CostModel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CostModel> costModel = new ArrayList<>();

    @OneToMany(mappedBy = "user", targetEntity = WalletModel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WalletModel> walletModel = new ArrayList<>();

    @OneToMany(mappedBy = "user", targetEntity = NotificationModel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NotificationModel> notificationModel = new ArrayList<>();

    @OneToMany(mappedBy = "user", targetEntity = RecommendationModel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RecommendationModel> recommendationModel = new ArrayList<>();

    @OneToMany(mappedBy = "user", targetEntity = PortfolioModel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PortfolioModel> portfolioModel = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleModel.class)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleModel> roles = new HashSet<>();
}
