package com.example.promotionpage.domain.news.api;

import com.example.promotionpage.domain.news.application.NewsService;
import com.example.promotionpage.domain.news.domain.News;
import com.example.promotionpage.domain.news.dto.CreateNewsRequestDto;
import com.example.promotionpage.domain.news.dto.UpdateNewsRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "News API", description = "News 등록 / 수정 / 삭제 / 조회")
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "News 등록 API")
    @PostMapping("")
    public ApiResponse<News> createNews(@Valid @RequestPart CreateNewsRequestDto dto,
                                        @RequestPart(value = "files", required = false) List<MultipartFile> files ) throws IOException {
        return newsService.createNews(dto.toServiceNews(), files);
    }

    @Operation(summary = "News 전체 조회 API")
    @GetMapping("/all")
    public ApiResponse<List<News>> retrieveAllNews() {
        return newsService.retrieveAllNews();
    }
}
