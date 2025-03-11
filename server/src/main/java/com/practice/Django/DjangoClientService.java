package com.practice.Django;
import com.practice.Django.Recommendation.UserRecommendationsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DjangoClientService {
    private final WebClient webClient;

    public DjangoClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://iupi-abf3.onrender.com").build();
    }

    public Mono<UserOnboardingResponse> onboardUser(UserOnboardingRequest request) {
        log.info("📤 Enviando datos a Django: {}", request);
        return webClient.post()
                .uri("/api/onboarding/")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserOnboardingResponse.class)
                .doOnError(error -> log.error("❌ Error en la respuesta de Django: {}", error.getMessage()));
    }

    public Mono<UserRecommendationsResponse> getRecommendations(Long userId) {
        return webClient.get()
                .uri("/api/recommendations/{userId}/", userId)
                .retrieve()
                .bodyToMono(UserRecommendationsResponse.class);
    }

    public Mono<UserInvestmentResponse> invest(UserInvestmentRequest request) {
        return webClient.post()
                .uri("/api/invest/")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserInvestmentResponse.class);
    }
}
