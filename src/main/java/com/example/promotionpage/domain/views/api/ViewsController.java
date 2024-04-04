package com.example.promotionpage.domain.views.api;

import com.example.promotionpage.domain.request.dto.request.CreateRequestDto;
import com.example.promotionpage.domain.views.application.ViewsService;
//import com.example.promotionpage.domain.views.dto.request.CreateViewsDto;
import com.example.promotionpage.domain.views.dto.request.CreateViewsRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "조회수 API", description = "조회수 등록 / 수정  / 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ViewsController {
    private final ViewsService viewsService;

    @Operation(summary = "조회수 등록 API")
    @PostMapping("/views")
    public ApiResponse createViews(@Valid @RequestBody CreateViewsRequestDto dto) throws IOException {
        return viewsService.createViews(dto.toServiceViews());
    }

//    @Operation(summary = "조회수 삭제 API")
//    @DeleteMapping("/views/{viewsId}")
//    public ApiResponse deleteRequest(@PathVariable Long viewsId){
//        return viewsService.deleteViews(requestId);
//    }

    @Operation(summary = "조회수 전체 조회 API")
    @GetMapping("/views")
    public ApiResponse retrieveAllViews(){
        return viewsService.retrieveAllViews();
    }

    @Operation(summary = "id로 조회수 상세 조회 API")
    @GetMapping("/views/id/{viewsId}")
    public ApiResponse retrieveViewsById(@PathVariable Long viewsId){
        return viewsService.retrieveViewsById(viewsId);
    }

    @Operation(summary = "해당 연도 조회수 전체 조회 API")
    @GetMapping("/views/{year}")
    public ApiResponse retrieveViewsByYear(@PathVariable Integer year){
        return viewsService.retrieveViewsByYear(year);
    }

    @Operation(summary = "연도, 월로 조회수 상세 조회 API")
    @GetMapping("/views/{year}/{month}")
    public ApiResponse retrieveViewsByYearMonth(@PathVariable Integer year, @PathVariable Integer month){
        return viewsService.retrieveViewsByYearMonth(year, month);
    }

    @Operation(summary = "기간(시작점(연도,월), 종료점(연도,월))으로 조회수 조회 API")
    @GetMapping("/views/{startYear}/{startMonth}/{endYear}/{endMonth}")
    public ApiResponse retrieveViewsByPeriod(@PathVariable Integer startYear, @PathVariable Integer startMonth,
                                             @PathVariable Integer endYear, @PathVariable Integer endMonth){
        return viewsService.retrieveViewsByPeriod(startYear, startMonth, endYear, endMonth);
    }

    @Operation(summary = "id로 특정 월 조회수 1 상승 API")
    @PutMapping("/views/increase/{viewsId}")
    public ApiResponse updateViewsById(@PathVariable Long viewsId){
        return viewsService.updateViewsById(viewsId);
    }

    @Operation(summary = "연도, 월로 특정 월 조회수 1 상승 API")
    @PutMapping("/views/increase/{year}/{month}")
    public ApiResponse updateViewsByYearMonth(@PathVariable Integer year, @PathVariable Integer month){
        return viewsService.updateViewsByYearMonth(year, month);
    }

    @Operation(summary = "이번 월 조회수 1 상승 API (해당 월이 존재하지 않을 경우에는 생성)")
    @PutMapping("/views/increase")
    public ApiResponse updateThisMonthViews(){
        return viewsService.updateThisMonthViews();
    }
}
