package com.practice.User.controller;

import com.practice.User.dtoRequest.AuthCreateUserRequestDto;
import com.practice.User.dtoRequest.UserRequestDto;
import com.practice.User.dtoResponse.UserPageResponse;
import com.practice.User.dtoResponse.UserResponseDto;
import com.practice.User.mapper.UserMapper;
import com.practice.User.model.UserModel;
import com.practice.User.repository.UserRepository;
import com.practice.User.service.UserService;
import com.practice.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "User Controller")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista paginada de usuarios.")
    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente")
    @GetMapping
    public ResponseEntity<UserPageResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        UserPageResponse response = userService.getAllUsers(page, size);
        return ResponseEntity.ok(response);
    }

    // Obtener un usuario por ID
    @Operation(summary = "Obtener un usuario por ID", description = "Devuelve un usuario por su ID.")
    @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Actualizar un usuario
    @Operation(summary = "Actualizar un usuario", description = "Actualiza los detalles de un usuario existente.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PatchMapping("/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequestDto updatedUserDto) {
        UserModel updatedUser = userService.updateUser(id, updatedUserDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Eliminar un usuario
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario por su ID.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Obtener información del usuario de google", description = "Devuelve la información del usuario autenticado.")
    @ApiResponse(responseCode = "200", description = "Información del usuario obtenida exitosamente")
    @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal OAuth2User oauthUser) {
        if (oauthUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("error", "Usuario no autenticado")
            );
        }
        Map<String, Object> userInfo = userService.processUser(oauthUser);

        return ResponseEntity.ok(userInfo);
    }


}
