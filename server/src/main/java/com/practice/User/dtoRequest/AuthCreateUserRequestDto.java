package com.practice.User.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
@Schema(description = "DTO para crear un usuario")
public record AuthCreateUserRequestDto(String photoUrl,
                                       @NotBlank(message = "El nombre es obligatorio")
                                       @Schema(description = "Nombre del usuario", required = true, example = "Juan")
                                       String name,
                                       @NotBlank(message = "El apellido es obligatorio")
                                       @Schema(description = "Apellido del usuario", required = true, example = "Perez")
                                       String lastName,
                                       @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no es valido")
                                       @Schema(description = "Correo del usuario", required = true, example = "7o5f1@example.com")
                                       String email,
                                       @NotBlank(message = "La contraseña es obligatoria")
                                       @Schema(description = "Contraseña del usuario", required = true, example = "password123")
                                       String password,
                                       @NotNull(message = "El telefono es obligatorio")
                                       @Schema(description = "Telefono del usuario", required = true, example = "1234567890")
                                       Integer  phoneNumber,
                                       @NotBlank(message = "El pais es obligatorio")
                                       @Schema(description = "Pais del usuario", required = true, example = "Colombia")
                                       String country,
                                       @NotNull(message = "La fecha de nacimiento es obligatoria")
                                       @Schema(description = "Fecha de nacimiento del usuario", required = true, example = "1990-01-01T00:00:00")
                                       LocalDateTime birthDate,
                                       @Valid
                                       @Schema(description = "Roles del usuario", required = true)
                                       AuthCreateRoleRequestDto roleDto) {
}
