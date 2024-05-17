package com.example.promotionpage.domain.faq.api;

import com.example.promotionpage.domain.faq.application.FaqService;
import com.example.promotionpage.domain.faq.dto.request.CreateFaqRequestDto;
import com.example.promotionpage.domain.faq.dto.request.UpdateFaqRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "FAQ API", description = "FAQ 등록 / 수정 / 삭제 / 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @Operation(summary = "FAQ 등록 API")
    @PostMapping("/faq")
    public ApiResponse createFaq(@Valid @RequestBody CreateFaqRequestDto dto) {
        return faqService.createFaq(dto.toServiceFaq());
    }

    @Operation(summary = "FAQ 전체 조회 API")
    @GetMapping("/faq")
    public ApiResponse retrieveAllFaq() {
        return faqService.retrieveAllFaq();
    }

    @Operation(summary = "FAQ 제목(id, 제목) 전체 조회 API")
    @GetMapping("/faq/title")
    public ApiResponse retrieveAllFaqTitle() {
        return faqService.retrieveAllFaqTitle();
    }

    @Operation(summary = "id로 FAQ 상세 조회 API")
    @GetMapping("/faq/{id}")
    public ApiResponse retrieveFaqById(@PathVariable Long id) {
        return faqService.retrieveFaqById(id);
    }

    @Operation(summary = "FAQ 수정 API")
    @PutMapping("/faq")
    public ApiResponse updateFaq(@Valid @RequestBody UpdateFaqRequestDto dto) {
        return faqService.updateFaq(dto.toServiceRequest());
    }

    @Operation(summary = "id로 FAQ 삭제 API")
    @DeleteMapping("/faq/{id}")
    public ApiResponse deleteFaq(@PathVariable Long id) {
        return faqService.deleteFaq(id);
    }

    @Operation(summary = "id로 FAQ 복수 삭제 API")
    @DeleteMapping("/faq")
    public ApiResponse deleteFaqs(@RequestBody List<Long> ids) {
        return faqService.deleteFaqs(ids);
    }
}
