package com.practice.Django;

import com.practice.Django.Recommendation.UserRecommendationsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/django")
@Slf4j
public class DjangoController {
    private final DjangoClientService djangoClientService;

    public DjangoController(DjangoClientService djangoClientService) {
        this.djangoClientService = djangoClientService;
    }

//    @PostMapping("/onboarding")
//    public Mono<UserOnboardingResponse> onboardUser(@RequestBody UserOnboardingRequest request) {
//        return djangoClientService.onboardUser(request);
//    }

    // Lombok crea el logger automáticamente
    @GetMapping("/recommendations/{userId}")
    public Mono<UserRecommendationsResponse> getRecommendations(@PathVariable Long userId) {
        log.info("🟢 Recibida solicitud de recomendaciones para userId: {}", userId);

        return djangoClientService.getRecommendations(userId)
                .doOnSubscribe(subscription -> log.info("🔵 Llamando a Django para obtener recomendaciones..."))
                .doOnSuccess(response -> log.info("✅ Respuesta recibida de Django (raw): {}", response))
                .doOnError(error -> log.error("❌ Error al obtener recomendaciones de Django: ", error));
    }

    @PostMapping("/invest")
    public Mono<UserInvestmentResponse> invest(@RequestBody UserInvestmentRequest request) {
        return djangoClientService.invest(request);
    }
}
