package com.practice.Notification.service;

import com.practice.Notification.Enum.EnumTypeNotification;
import com.practice.Notification.dtoRequest.NotificationCreateRequestDto;
import com.practice.Notification.dtoRequest.NotificationRequestDto;
import com.practice.Notification.dtoRequest.NotificationUpdateRequestDto;
import com.practice.Notification.dtoResponse.NotificationCreateResponseDto;
import com.practice.Notification.dtoResponse.NotificationPageResponseDto;
import com.practice.Notification.dtoResponse.NotificationResponseDto;
import com.practice.Notification.mappers.NotificationMapper;
import com.practice.Notification.model.NotificationModel;
import com.practice.Notification.repository.NotificationRepository;
import com.practice.User.model.UserModel;
import com.practice.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @Override
    public NotificationPageResponseDto findAllNotification(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<NotificationModel> notificationPage = notificationRepository.findAll(pageable);

        List<NotificationRequestDto> notificationDtos = notificationPage.getContent()
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());

        return new NotificationPageResponseDto(notificationDtos, notificationPage.getTotalPages(), notificationPage.getTotalElements());
    }

    @Override
    public NotificationResponseDto getNotificationById(Long id) {
        NotificationModel notificationModel = notificationRepository.findById(id).orElseThrow();
        return notificationMapper.toDtoNotification(notificationModel);
    }

    @Override
    public NotificationCreateResponseDto createNotification(NotificationCreateRequestDto notificationCreateRequestDto) {
        UserModel userEntity = userRepository.findById(notificationCreateRequestDto.idUser()).orElseThrow();
        EnumTypeNotification typeNotification;
        try {
            typeNotification = EnumTypeNotification.valueOf(notificationCreateRequestDto.typeNotification());
        } catch (
                IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de notification Invalido");
        }

        NotificationModel notificationModel = NotificationModel
                .builder()
                .type(typeNotification)
                .message(notificationCreateRequestDto.message())
                .dateCreate(LocalDateTime.now())
                .isRead(notificationCreateRequestDto.isRead())
                .user(userEntity)
                .build();
        notificationRepository.save(notificationModel);
        return new NotificationCreateResponseDto(
                notificationModel.getId(), notificationCreateRequestDto.typeNotification(),
                notificationModel.getMessage(), notificationModel.getIsRead(), notificationCreateRequestDto.idUser());
    }

    @Override
    public NotificationResponseDto updateNotification(Long id, NotificationUpdateRequestDto notificationUpdateRequestDto) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    if(notificationUpdateRequestDto.getTypeNotification()!= null){
                    EnumTypeNotification typeNotification = EnumTypeNotification.valueOf(notificationUpdateRequestDto.getTypeNotification());
                    notification.setType(typeNotification);

                    }
                    notification.setMessage(notificationUpdateRequestDto.getMessage());
                    notification.setIsRead(notificationUpdateRequestDto.getIsRead());
                    if (notificationUpdateRequestDto.getIdUser() != null) {
                        UserModel userUpdate = userRepository.findById(
                                notificationUpdateRequestDto.getIdUser()
                        ).orElseThrow();
                        notification.setUser(userUpdate);
                    }
                    NotificationModel updateNotification = notificationRepository.save(notification);
                    return notificationMapper.toDtoNotification(updateNotification);
                }).orElseThrow(() -> new NoSuchElementException("Notification con id " + notificationUpdateRequestDto.getId() + "no se pudo encontrar"));
    }

    @Override
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new IllegalArgumentException("La notification con id " + id + "no existe");
        }
        notificationRepository.deleteById(id);
    }
}