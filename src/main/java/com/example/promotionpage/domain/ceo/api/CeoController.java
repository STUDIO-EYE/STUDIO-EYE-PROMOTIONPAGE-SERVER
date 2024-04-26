package com.example.promotionpage.domain.ceo.api;

import com.example.promotionpage.domain.ceo.application.CeoService;
import com.example.promotionpage.domain.ceo.dto.request.CreateCeoRequestDto;
import com.example.promotionpage.domain.faq.domain.Faq;
import com.example.promotionpage.domain.project.dto.request.CreateProjectRequestDto;
import com.example.promotionpage.global.adapter.S3Adapter;
import com.example.promotionpage.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "CEO 정보 API", description = "CEO 정보 등록 / 수정 / 삭제 / 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CeoController {
    private final CeoService ceoService;

    @Operation(summary = "CEO 정보 등록 API")
    @PostMapping("/ceo")
    public ApiResponse createCeoInformation(@Valid @RequestPart("request") CreateCeoRequestDto dto,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files){
        return ceoService.createCeoInformation(dto.toServiceRequest(), files);
    }

    @Operation(summary = "CEO 전체 정보 조회 API")
    @GetMapping("/ceo")
    public ApiResponse retrieveCeoInformation() {
        return ceoService.retrieveCeoInformation();
    }
}
