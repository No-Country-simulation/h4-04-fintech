package com.practice.User.service;

import com.practice.User.dtoRequest.UserRequestDto;
import com.practice.User.dtoResponse.UserPageResponse;
import com.practice.User.dtoResponse.UserResponseDto;
import com.practice.User.mapper.UserMapper;
import com.practice.User.model.RoleModel;
import com.practice.User.model.UserModel;
import com.practice.User.repository.UserRepository;
import com.practice.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserPageResponse getAllUsers(int page, int size) {
        // Crear Pageable con los parámetros proporcionados
        Pageable pageable = PageRequest.of(page, size);

        // Obtener la página de usuarios
        Page<UserModel> usersPage = userRepository.findAll(pageable);

        // Mapear las entidades a DTOs
        List<UserRequestDto> userDtos = usersPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        // Devolver la respuesta con la información de paginación
        return new UserPageResponse(userDtos, usersPage.getTotalPages(), usersPage.getTotalElements());
    }

    public UserResponseDto getUserById(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado."));

        return userMapper.toDtoUser(user);

    }

    public UserModel updateUser(Long id, UserRequestDto updatedUserDto) {
        UserModel updatedUser = userMapper.toEntity(updatedUserDto);
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setLastName(updatedUser.getLastName());
                    user.setEmail(updatedUser.getEmail());
                    user.setPhoneNumber(updatedUser.getPhoneNumber());
                    user.setPhotoUrl(updatedUser.getPhotoUrl());
                    user.setBirthDate(updatedUser.getBirthDate());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado."));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario con ID " + id + " no encontrado.");
        }
        userRepository.deleteById(id);
    }

    public Map<String, Object> processUser(OAuth2User oauthUser) {
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        // Buscar usuario en la BD o crearlo si no existe
        UserModel user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserModel newUser = new UserModel();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setPhotoUrl(picture);
                    return userRepository.save(newUser); // Guarda en BD
                });

        // Construir la respuesta con los datos del usuario
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("email", user.getEmail());
        userInfo.put("name", user.getName());

        if (user.getPhotoUrl() != null) {
            userInfo.put("picture", user.getPhotoUrl());
        }

        return userInfo;
    }
}
