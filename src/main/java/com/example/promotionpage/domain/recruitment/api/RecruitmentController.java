package com.example.promotionpage.domain.recruitment.api;

import com.example.promotionpage.domain.recruitment.application.RecruitmentService;
import com.example.promotionpage.domain.recruitment.domain.Recruitment;
import com.example.promotionpage.domain.recruitment.dto.request.CreateRecruitmentRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채용공고 게시판 API", description = "채용공고 게시물 등록 / 수정 / 삭제 / 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @Operation(summary = "채용공고 등록 API")
    @PostMapping("/recruitment")
    public ApiResponse<Recruitment> createRecruitment(@Valid @RequestBody CreateRecruitmentRequestDto dto) {
        return recruitmentService.createRecruitment(dto.toServiceRequest());
    }
}
