package com.practice.Notices.mappers;

import com.practice.Notices.dtoRequest.NoticieRequestDto;
import com.practice.Notices.model.NoticieModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class NoticeMapper {
    private final ModelMapper modelMapper;

    public NoticeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public NoticieRequestDto toDto(NoticieModel noticieModel) {
        return modelMapper.map(noticieModel, NoticieRequestDto.class);
    }


}
