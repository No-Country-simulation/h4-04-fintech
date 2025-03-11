package com.practice.FinancingProfile.service;

import com.practice.FinancingProfile.dtoRequest.FinancingProfileOnboardingDto;
import com.practice.FinancingProfile.dtoRequest.FinancingProfileRequestDto;
import com.practice.FinancingProfile.dtoResponse.FinancingProfilePageResponse;
import com.practice.FinancingProfile.dtoResponse.FinancingProfileResponseDto;
import com.practice.FinancingProfile.model.FinancingProfileModel;
import com.practice.FinancingProfile.repository.FinancingProfileRepository;
import com.practice.User.model.UserModel;
import com.practice.User.repository.UserRepository;
import com.practice.exceptions.FinancingProfileNotFoundException;
import com.practice.exceptions.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancingProfileServiceImpl implements FinancingProfileService {
    private final FinancingProfileRepository financingProfileRepository;
    private final ModelMapper financingProfileMapper;
    private final UserRepository userRepository;

    private static final String USER_NOT_FOUND = "El usuario con ID %d no fue encontrado";
    private static final String FINANCING_PROFILE_NOT_FOUND = "El perfil financiero con ID %d no fue encontrado";

    @Override
    public FinancingProfilePageResponse findAllFinancingProfiles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FinancingProfileModel> financingPage = financingProfileRepository.findAll(pageable);

        List<FinancingProfileResponseDto> financingProfileDtos = financingPage.getContent()
                .stream()
                .map(financing -> financingProfileMapper.map(financing, FinancingProfileResponseDto.class))
                .collect(Collectors.toList());

        return new FinancingProfilePageResponse(
                financingProfileDtos,
                financingPage.getTotalPages(),
                financingPage.getTotalElements());
    }

    @Override
    public FinancingProfileResponseDto findFinancingProfileById(Long id) {
        FinancingProfileModel financingProfileModel = financingProfileRepository.findById(id)
                .orElseThrow(() -> new FinancingProfileNotFoundException(String.format(FINANCING_PROFILE_NOT_FOUND, id)));
        return financingProfileMapper.map(financingProfileModel, FinancingProfileResponseDto.class);
    }

    @Override
    public FinancingProfileResponseDto saveOnboarding(@Valid FinancingProfileOnboardingDto onboardingDto) {
        UserModel userModel = userRepository.findById(onboardingDto.getUserId()).orElseThrow(() ->
                new UserNotFoundException("El usuario con ID " + onboardingDto.getUserId() + " no fue encontrado."));

        FinancingProfileModel financingProfileModel = financingProfileMapper.map(onboardingDto, FinancingProfileModel.class);
        financingProfileModel.setUser(userModel);

        FinancingProfileModel savedFinancingProfile = financingProfileRepository.save(financingProfileModel);

        return financingProfileMapper.map(savedFinancingProfile, FinancingProfileResponseDto.class);
    }

    @Override
    public FinancingProfileResponseDto updateFinancingProfile(Long id, @Valid FinancingProfileRequestDto financingProfileRequest) {
        FinancingProfileModel financingProfileModel = financingProfileRepository.findById(id)
                .orElseThrow(() -> new FinancingProfileNotFoundException(String.format(FINANCING_PROFILE_NOT_FOUND, id)));

        financingProfileMapper.map(financingProfileRequest, financingProfileModel);

        if (financingProfileRequest.getUserId() != null) {
            UserModel user = userRepository.findById(financingProfileRequest.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, financingProfileRequest.getUserId())));
            financingProfileModel.setUser(user);
        }

        FinancingProfileModel savedFinancingProfile = financingProfileRepository.save(financingProfileModel);

        return financingProfileMapper.map(savedFinancingProfile, FinancingProfileResponseDto.class);
    }

    @Override
    public void deletePortfolio(Long id) {
        FinancingProfileModel financingProfileModel = financingProfileRepository.findById(id)
                .orElseThrow(() -> new FinancingProfileNotFoundException(String.format(FINANCING_PROFILE_NOT_FOUND, id)));
        financingProfileRepository.delete(financingProfileModel);
    }

    @Override
    public FinancingProfileResponseDto findFinancingProfileByUserId(Long userId) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));
        FinancingProfileModel financingProfile = financingProfileRepository.findByUser(user).orElse(null);
        return financingProfileMapper.map(financingProfile, FinancingProfileResponseDto.class);
    }


}
