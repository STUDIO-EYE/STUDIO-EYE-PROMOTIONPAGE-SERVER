package com.example.promotionpage.domain.faq.application;

import com.example.promotionpage.domain.faq.dao.FaqRepository;
import com.example.promotionpage.domain.faq.dao.FaqTitles;
import com.example.promotionpage.domain.faq.domain.Faq;
import com.example.promotionpage.domain.faq.dto.request.CreateFaqServiceRequestDto;
import com.example.promotionpage.domain.faq.dto.request.UpdateFaqRequestDto;
import com.example.promotionpage.domain.faq.dto.request.UpdateFaqServiceRequestDto;
import com.example.promotionpage.domain.views.domain.Views;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    public ApiResponse createFaq(CreateFaqServiceRequestDto dto){
        if(dto.title().trim().isEmpty() || dto.content().trim().isEmpty() || dto.visibility() == null) {
            return ApiResponse.withError(ErrorCode.FAQ_IS_EMPTY);
        }
        Faq faq = dto.toEntity();
        Faq savedFaq = faqRepository.save(faq);
        return ApiResponse.ok("FAQ를 성공적으로 등록하였습니다.", savedFaq);
    }

    public ApiResponse retrieveAllFaq() {
        List<Faq> faqList = faqRepository.findAll();
        if(faqList.isEmpty()) {
            return ApiResponse.ok("FAQ가 존재하지 않습니다.");
        }
        return ApiResponse.ok("FAQ 목록을 성공적으로 조회했습니다.", faqList);
    }


    public ApiResponse retrieveAllFaqTitle() {
        List<FaqTitles> faqTitles = faqRepository.findAllTitles();
        if(faqTitles.isEmpty()) {
            return ApiResponse.ok("FAQ가 존재하지 않습니다.");
        }
        return ApiResponse.ok("FAQ 목록을 성공적으로 조회했습니다.", faqTitles);
    }

    public ApiResponse retrieveFaqById(Long id) {
        Optional<Faq> optionalFaq = faqRepository.findById(id);
        if(optionalFaq.isEmpty()) {
            return ApiResponse.ok("FAQ가 존재하지 않습니다.");
        }
        Faq faq = optionalFaq.get();
        return ApiResponse.ok("FAQ를 성공적으로 조회했습니다.", faq);
    }

    public ApiResponse updateFaq(UpdateFaqServiceRequestDto dto) {
        String title = dto.title().trim();
        String content = dto.content().trim();
        Boolean visibility = dto.visibility();
        Optional<Faq> optionalFaq = faqRepository.findById(dto.id());
        if(optionalFaq.isEmpty()) {
            return ApiResponse.withError(ErrorCode.INVALID_FAQ_ID);
        }

        Faq faq = optionalFaq.get();
        if(!title.isEmpty()) {
            faq.updateTitle(title);
        }
        if(!content.isEmpty()) {
            faq.updateContent(content);
        }
        if(visibility != null) {
            faq.updateVisibility(visibility);
        }
        Faq updatedFaq = faqRepository.save(faq);
        return ApiResponse.ok("FAQ를 성공적으로 수정하였습니다.", updatedFaq);
    }

    public ApiResponse deleteFaq(Long id) {
        Optional<Faq> optionalFaq = faqRepository.findById(id);
        if(optionalFaq.isEmpty()) {
            return ApiResponse.withError(ErrorCode.INVALID_FAQ_ID);
        }
        Faq faq = optionalFaq.get();
        faqRepository.delete(faq);
        return ApiResponse.ok("FAQ를 성공적으로 삭제했습니다.");
    }
}
