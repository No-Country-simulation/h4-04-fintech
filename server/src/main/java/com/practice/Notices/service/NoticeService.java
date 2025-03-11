package com.practice.Notices.service;

import com.practice.Notices.dtoResponse.NoticePageResponse;

public interface NoticeService {
    NoticePageResponse findAllNoticies(int page, int size);
}
