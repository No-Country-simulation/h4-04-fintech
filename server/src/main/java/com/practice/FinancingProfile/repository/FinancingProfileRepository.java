package com.practice.FinancingProfile.repository;

import com.practice.FinancingProfile.model.FinancingProfileModel;
import com.practice.User.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinancingProfileRepository extends JpaRepository<FinancingProfileModel, Long> {
    Optional<FinancingProfileModel> findByUser(UserModel user);
}
