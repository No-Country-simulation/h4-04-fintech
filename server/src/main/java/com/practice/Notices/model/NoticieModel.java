package com.practice.Notices.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@Table(name = "notice")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class NoticieModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "author")
    private String author;
    @Column(name = "published_at")
    private LocalDate publishedAt;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "comments")
    private String comments;
    @Column(name = "image_url")
    private String imageUrl;
}
