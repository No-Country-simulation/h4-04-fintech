package com.practice.Notices.dtoResponse;


import com.practice.Notices.dtoRequest.NoticieRequestDto;

import java.util.List;

public record NoticePageResponse(
        List<NoticieRequestDto> noticies,
        int totalPages,
        long totalElements
) {

}
