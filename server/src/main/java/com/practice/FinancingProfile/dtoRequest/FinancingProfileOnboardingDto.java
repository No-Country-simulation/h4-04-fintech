package com.practice.FinancingProfile.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para el onboarding del perfil de financiamiento")
public class FinancingProfileOnboardingDto {
    @Schema(description = "ID del usuario", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del usuario es requerido")
    private Long userId;

    @Schema(description = "Nivel de conocimiento del perfil", example = "MODERADO", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nivel de riego es requerido")
    private String riskProfile;

    @Schema(description = "El ingreso mensual del perfil", example = "5000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Los ingresos mensuales son requeridos")
    private Double incomeMonthly;

    @Schema(description = "El gasto mensual del perfil", example = "3000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Los gastos mensuales son requeridos")
    private Double expensesMonthly;

    @Schema(description = "El porcentaje de ahorro del perfil", example = "20.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El porcentaje de ahorro es requerido")
    private Double percentageSave;
}
