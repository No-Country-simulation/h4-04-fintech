package com.practice.Notices.dtoRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticieRequestDto {
    private Long id;
    private String author;
    private LocalDate publishedAt;
    private String title;
    private String description;
    private String comments;
    private String imageUrl;
}
