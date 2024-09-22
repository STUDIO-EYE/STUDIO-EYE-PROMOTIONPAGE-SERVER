package com.example.promotionpage.domain.news.application;

import com.example.promotionpage.domain.news.dao.NewsFileRepository;
import com.example.promotionpage.domain.news.dao.NewsRepository;
import com.example.promotionpage.domain.news.domain.News;
import com.example.promotionpage.domain.news.domain.NewsFile;
import com.example.promotionpage.domain.news.dto.CreateNewsServiceRequestDto;
import com.example.promotionpage.domain.news.dto.UpdateNewsServiceRequestDto;
import com.example.promotionpage.global.adapter.S3Adapter;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsFileRepository newsFileRepository;
    private final S3Adapter s3Adapter;

    public ApiResponse<News> createNews(CreateNewsServiceRequestDto dto, List<MultipartFile> files) throws IOException {
        if(dto.title().trim().isEmpty() || dto.content().trim().isEmpty() || dto.visibility() == null) {
            return ApiResponse.withError(ErrorCode.NEWS_IS_EMPTY);
        }
        News news = dto.toEntity();
        newsRepository.save(news);

        // 파일 업로드
        List<NewsFile> newsFiles = new ArrayList<>();

        if (files != null){
            for (MultipartFile file : files) {
//                String s3key = s3Adapter.uploadFile(file);
                newsFiles.add(NewsFile.builder()
                        .fileName(file.getOriginalFilename())
                        .filePath(s3Adapter.uploadFile(file).getData())
//                        .s3key(s3key)
                        .news(news)
                        .build());
            }
            newsFileRepository.saveAllAndFlush(newsFiles);
        }
        return ApiResponse.ok("News를 성공적으로 등록하였습니다.");
    }

    public ApiResponse<List<News>> retrieveAllNews() {
        List<News> newsList = newsRepository.findAll();
        if(newsList.isEmpty()) {
            return ApiResponse.ok("News가 존재하지 않습니다.");
        }
        return ApiResponse.ok("News 목록을 성공적으로 조회했습니다.", newsList);
    }
}
