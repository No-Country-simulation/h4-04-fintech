package com.practice.Django;

import com.practice.FinancingProfile.dtoResponse.FinancingProfileResponseDto;
import com.practice.User.dtoResponse.AuthResponseDto;
import com.practice.User.model.UserModel;
import com.practice.User.repository.UserRepository;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class    UserOnboardingRequest {

    private Long userId;
    private String riskProfile;
    private Double incomeMonthly;
    private Double expensesMonthly;
    private Double percentageSave;
    private LocalDate birthDate;
    private String investmentExperience;

//    // Método estático para construir desde varios DTOs, recibiendo UserRepository como parámetro
//    public static UserOnboardingRequest fromMultipleDTOs(
//            FinancingProfileResponseDto personalInfo,
//            UserRepository userRepository // Inyectamos el repositorio como parámetro
//    ) {
//        UserOnboardingRequest request = new UserOnboardingRequest();
//        request.userId = personalInfo.getUserId();
//        request.incomeMonthly = personalInfo.getIncomeMonthly();
//        request.expensesMonthly = personalInfo.getExpensesMonthly();
//        request.percentageSave = personalInfo.getPercentageSave();
//        request.investmentExperience = generarExperienciaInversion();
//        request.riskProfile = personalInfo.getRiskProfile();
//
//        Optional<UserModel> userEntity = userRepository.findById(request.userId);
//        request.birthDate = userEntity
//                .map(user -> user.getBirthDate().toLocalDate())
//                .orElseGet(UserOnboardingRequest::generarFechaAleatoria); // Llamamos al método estático
//
//        return request;
//    }

    // Método para generar una fecha aleatoria
    public static LocalDate generarFechaAleatoria() {
        Random random = new Random();
        int edadAleatoria = random.nextInt(16) + 25; // Genera edades entre 25 y 40 años
        return LocalDate.now().minusYears(edadAleatoria);
    }

    // Método para generar el campo investmentExperience aleatorio
    public static String generarExperienciaInversion() {
        String[] experiencias = {"PRINCIPIANTE", "INTERMEDIO", "AVANZADO"};
        Random random = new Random();
        return experiencias[random.nextInt(experiencias.length)]; // Selecciona un valor aleatorio del array
    }
}
