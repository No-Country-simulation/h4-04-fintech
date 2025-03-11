package com.practice.Notices.service;

import com.practice.Notices.dtoRequest.NoticieRequestDto;
import com.practice.Notices.dtoResponse.NoticePageResponse;
import com.practice.Notices.mappers.NoticeMapper;
import com.practice.Notices.model.NoticieModel;
import com.practice.Notices.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;

    @Override
    public NoticePageResponse findAllNoticies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NoticieModel> noticiePage = noticeRepository.findAll(pageable);
        List<NoticieRequestDto> noticieDtos = noticiePage.getContent().stream().map(
                noticeMapper::toDto
        ).collect(Collectors.toList());
        return new NoticePageResponse(noticieDtos, noticiePage.getTotalPages(), noticiePage.getTotalElements());
    }
}
