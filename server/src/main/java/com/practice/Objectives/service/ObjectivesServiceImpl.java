package com.practice.Objectives.service;

import com.practice.FinancingProfile.model.FinancingProfileModel;
import com.practice.FinancingProfile.repository.FinancingProfileRepository;
import com.practice.Objectives.Enum.EnumFrequency;
import com.practice.Objectives.Enum.EnumObjectiveType;
import com.practice.Objectives.Enum.EnumPriority;
import com.practice.Objectives.dtoRequest.ObjectivesCreateRequestDto;
import com.practice.Objectives.dtoRequest.ObjectivesRequestDto;
import com.practice.Objectives.dtoRequest.ObjectivesUpdateRequestDto;
import com.practice.Objectives.dtoResponse.ObjectivePageResponse;
import com.practice.Objectives.dtoResponse.ObjectivesResponseCreateDto;
import com.practice.Objectives.dtoResponse.ObjectivesResponseDto;
import com.practice.Objectives.mappers.ObjectiveMapper;
import com.practice.Objectives.model.ObjectiveModel;
import com.practice.Objectives.repository.ObjectiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObjectivesServiceImpl implements ObjectivesService {
    private final ObjectiveRepository objectiveRepository;
    private final ObjectiveMapper objectiveMapper;
    private final FinancingProfileRepository financingProfileRepository;

    @Override
    public ObjectivePageResponse findAllObjectives(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ObjectiveModel> objectivePage = objectiveRepository.findAll(pageable);
        List<ObjectivesRequestDto> objectiveDtos = objectivePage.getContent()
                .stream()
                .map(objectiveMapper::toDto)
                .collect(Collectors.toList());

        return new ObjectivePageResponse(objectiveDtos, objectivePage.getTotalPages(), objectivePage.getTotalElements());
    }

    @Override
    public ObjectivesResponseDto findObjectiveById(Long id) {
        ObjectiveModel objective = objectiveRepository.findById(id).orElseThrow();
        return objectiveMapper.toDtoObjective(objective);
    }

    @Override
    public ObjectivesResponseCreateDto createObjective(ObjectivesCreateRequestDto objectivesCreateRequestDto) {
//        FinancingProfileModel financingProfileEntity = financingProfileRepository.findById(objectivesCreateRequestDto.idFinancingProfile()).orElseThrow();
        EnumPriority priority;
        EnumObjectiveType objectiveType;
        EnumFrequency frequency;
        try {
            priority = EnumPriority.valueOf(objectivesCreateRequestDto.priority());
            objectiveType = EnumObjectiveType.valueOf(objectivesCreateRequestDto.objectiveType());
            frequency = EnumFrequency.valueOf(objectivesCreateRequestDto.frequency());

        } catch (
                IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de notification Invalido");
        }
        ObjectiveModel objectiveModel = ObjectiveModel
                .builder()
                .description(objectivesCreateRequestDto.description())
                .enumObjectiveType(objectiveType)
                .amountObjective(objectivesCreateRequestDto.amountObjective())
                .annualProgress(objectivesCreateRequestDto.annualProgress())
                .enumPriority(priority)
                .enumFrequency(frequency)
//                .financingProfile(financingProfileEntity)
                .build();
        objectiveRepository.save(objectiveModel);

        return new ObjectivesResponseCreateDto(
                objectiveModel.getId(), objectiveModel.getDescription(),
                objectivesCreateRequestDto.objectiveType(),
                objectiveModel.getAmountObjective(),
                objectiveModel.getAnnualProgress(),
                objectivesCreateRequestDto.priority(),
                objectivesCreateRequestDto.frequency(),
                objectivesCreateRequestDto.startDate(),
                objectivesCreateRequestDto.targetDate()
        );
    }

    @Override
    public ObjectivesResponseDto updateObjective(Long id, ObjectivesUpdateRequestDto objectivesUpdateRequestDto) {
        return objectiveRepository.findById(id)
                .map(objective -> {
                    if (objectivesUpdateRequestDto.getObjectiveType() != null) {
                        System.out.println("Objective Type recibido :" + objectivesUpdateRequestDto.getObjectiveType());
                        EnumObjectiveType objectiveType = EnumObjectiveType.valueOf(objectivesUpdateRequestDto.getObjectiveType());
                        objective.setEnumObjectiveType(objectiveType);
                    }
                    if (objectivesUpdateRequestDto.getPriority() != null) {
                        EnumPriority priority = EnumPriority.valueOf(objectivesUpdateRequestDto.getPriority());
                        objective.setEnumPriority(priority);
                    }
                    if (objectivesUpdateRequestDto.getFrequency() != null) {
                        EnumFrequency frequency = EnumFrequency.valueOf(objectivesUpdateRequestDto.getFrequency());
                        objective.setEnumFrequency(frequency);
                    }
                    if (objectivesUpdateRequestDto.getDescription() != null) {
                        objective.setDescription(objectivesUpdateRequestDto.getDescription());
                    }
                    if (objectivesUpdateRequestDto.getAmountObjective() != null) {
                        objective.setAmountObjective(objectivesUpdateRequestDto.getAmountObjective());
                    }
                    if (objectivesUpdateRequestDto.getAnnualProgress() != null) {
                        objective.setAnnualProgress(objectivesUpdateRequestDto.getAnnualProgress());
                    }
                    if (objectivesUpdateRequestDto.getStartDate() != null) {
                        objective.setStartDate(objectivesUpdateRequestDto.getStartDate());
                    }
                    if (objectivesUpdateRequestDto.getTargetDate() != null) {
                        objective.setTargetDate(objectivesUpdateRequestDto.getTargetDate());
                    }
                    ObjectiveModel updateObjective = objectiveRepository.save(objective);
                    return objectiveMapper.toDtoObjective(updateObjective);
                }).orElseThrow(() -> new IllegalArgumentException("No se encontro el objetivo con id" + objectivesUpdateRequestDto.getId()));
    }

    @Override
    public void deleteObjective(Long id) {
        if (!objectiveRepository.existsById(id)) {
            throw new IllegalArgumentException("Objectivo con id" + id + "no existe");

        }
        objectiveRepository.deleteById(id);
    }
}
