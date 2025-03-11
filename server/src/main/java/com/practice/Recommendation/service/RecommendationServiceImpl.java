package com.practice.Recommendation.service;

import com.practice.Recommendation.dtoRequest.RecommendationRequestDto;
import com.practice.Recommendation.dtoRequest.RecommendationRequestPrueba;
import com.practice.Recommendation.dtoResponse.RecommendationPageResponseDto;
import com.practice.Recommendation.mappers.RecommendationMapper;
import com.practice.Recommendation.model.RecommendationModel;
import com.practice.Recommendation.repository.RecommendationRepository;
import com.practice.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;
    private final UserRepository userRepository;

//    @Value("${django.server.url}") // URL del servidor de Django (se configura en application.properties)
//    private String djangoServerUrl;


    @Override
    public RecommendationPageResponseDto findAllRecommendation(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecommendationModel> recommendationPage = recommendationRepository.findAll(pageable);
        List<RecommendationRequestDto> recommendationDtos = recommendationPage.getContent()
                .stream()
                .map(recommendationMapper::toDto)
                .collect(Collectors.toList());

        return new RecommendationPageResponseDto(recommendationDtos, recommendationPage.getTotalPages(),
                recommendationPage.getTotalElements());
    }
}
