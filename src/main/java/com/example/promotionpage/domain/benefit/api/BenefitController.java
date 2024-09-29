package com.example.promotionpage.domain.benefit.api;

import com.example.promotionpage.domain.benefit.application.BenefitService;
import com.example.promotionpage.domain.benefit.domain.Benefit;
import com.example.promotionpage.domain.benefit.dto.request.CreateBenefitRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "혜택 정보 API", description = "혜택 정보 등록 / 수정 / 삭제 / 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BenefitController {
    private final BenefitService benefitService;

    @Operation(summary = "혜택 정보 등록 API")
    @PostMapping("/benefit")
    public ApiResponse<Benefit> createBenefit(@Valid @RequestPart("request") CreateBenefitRequestDto dto,
                                              @RequestPart(value = "file", required = false) MultipartFile file){
        return benefitService.createBenefit(dto.toServiceRequest(), file);
    }
}
