package com.example.promotionpage.domain.companyInformation.api;

import com.example.promotionpage.domain.companyInformation.application.CompanyInformationService;
import com.example.promotionpage.domain.companyInformation.dto.request.CreateCompanyInformationRequestDto;
import com.example.promotionpage.domain.request.dto.request.CreateRequestDto;
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

import java.io.IOException;
import java.util.List;

@Tag(name = "회사 정보(기본 정보, 소개 정보, 상세 정보) API", description = "회사 정보(기본 정보, 소개 정보, 상세 정보) 등록 / 수정 / 삭제 / 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompanyInformationController {
    private CompanyInformationService companyInformationService;


    @Operation(summary = "문의 등록 API")
    @PostMapping("/requests")
    public ApiResponse createCompanyInformation(@Valid @RequestPart("request") CreateCompanyInformationRequestDto dto, @RequestPart(value = "files", required = false) List<MultipartFile> files) throws
            IOException {
        return companyInformationService.createRequest(dto.toServiceRequest(), files);
    }
}
