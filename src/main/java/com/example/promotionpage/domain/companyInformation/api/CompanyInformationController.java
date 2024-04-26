package com.example.promotionpage.domain.companyInformation.api;

import com.example.promotionpage.domain.companyInformation.application.CompanyInformationService;
import com.example.promotionpage.domain.companyInformation.dto.request.CreateCompanyInformationRequestDto;
import com.example.promotionpage.domain.request.dto.request.CreateRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "회사 정보(기본 정보, 소개 정보, 상세 정보) API", description = "회사 정보(기본 정보, 소개 정보, 상세 정보) 등록 / 수정 / 삭제 / 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompanyInformationController {
    private final CompanyInformationService companyInformationService;


    @Operation(summary = "회사 정보 등록 API")
    @PostMapping("/company/information")
    public ApiResponse createCompanyInformation(@Valid @RequestPart("request") CreateCompanyInformationRequestDto dto,
                                                @RequestPart(value = "logoImageUrl", required = false) MultipartFile logoImageUrl,
                                                @RequestPart(value = "sloganImageUrl", required = false) MultipartFile sloganImageUrl) throws IOException {
        return companyInformationService.createCompanyInformation(dto.toServiceRequest(), logoImageUrl, sloganImageUrl);
    }

    @Operation(summary = "회사 전체 정보 조회 API")
    @GetMapping("/company/information")
    public ApiResponse retrieveAllCampanyInformation() {
        return companyInformationService.retrieveAllCampanyInformation();
    }

    @Operation(summary = "회사 로고 이미지 조회 API")
    @GetMapping("/company/logo")
    public ApiResponse retrieveCampanyLogoImage() {
        return companyInformationService.retrieveCampanyLogoImage();
    }

    @Operation(summary = "회사 기본 정보(주소, 유선번호, 팩스번호) 조회 API")
    @GetMapping("/company/basic")
    public ApiResponse retrieveCompanyBasicInformation() {
        return companyInformationService.retrieveCompanyBasicInformation();
    }

    @Operation(summary = "회사 소개 정보 조회 API")
    @GetMapping("/company/introduction")
    public ApiResponse retrieveCompanyIntroductionInformation() {
        return companyInformationService.retrieveCompanyIntroductionInformation();
    }

    @Operation(summary = "회사 5가지 상세 정보 조회 API")
    @GetMapping("/company/detail")
    public ApiResponse retrieveCompanyDetailInformation() {
        return companyInformationService.retrieveCompanyDetailInformation();
    }
}
