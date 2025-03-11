package com.practice.Notices.controller;

import com.practice.Notices.dtoResponse.NoticePageResponse;
import com.practice.Notices.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notice")
@Tag(name = "Noticias", description = "Noticias API")
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(summary = "Obtener todas las noticias", description = "Devuelve todas las noticias")
    @ApiResponse(responseCode = "200", description = "Noticias obtenidas correctamente")
    @ApiResponse(responseCode = "404", description = "no se encontraron Noticias")
    @GetMapping("/")
    public ResponseEntity<NoticePageResponse> findAllNoticies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        NoticePageResponse response = noticeService.findAllNoticies(page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
