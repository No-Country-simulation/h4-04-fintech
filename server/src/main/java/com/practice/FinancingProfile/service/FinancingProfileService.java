package com.practice.FinancingProfile.service;

import com.practice.FinancingProfile.dtoRequest.FinancingProfileOnboardingDto;
import com.practice.FinancingProfile.dtoRequest.FinancingProfileRequestDto;
import com.practice.FinancingProfile.dtoResponse.FinancingProfilePageResponse;
import com.practice.FinancingProfile.dtoResponse.FinancingProfileResponseDto;
import jakarta.validation.Valid;

public interface FinancingProfileService {
    FinancingProfilePageResponse findAllFinancingProfiles(int page, int size);

    FinancingProfileResponseDto findFinancingProfileById(Long id);

    FinancingProfileResponseDto saveOnboarding(@Valid FinancingProfileOnboardingDto onboardingDto);

    //FinancingProfileResponseDto saveFinancingProfile(@Valid FinancingProfileRequestDto financingProfileRequest);

    FinancingProfileResponseDto updateFinancingProfile(Long id, @Valid FinancingProfileRequestDto financingProfileRequest);

    void deletePortfolio(Long id);

    FinancingProfileResponseDto findFinancingProfileByUserId(Long userId);
}
