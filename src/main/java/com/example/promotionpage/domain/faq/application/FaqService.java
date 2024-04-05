package com.example.promotionpage.domain.faq.application;

import com.example.promotionpage.domain.faq.dao.FaqRepository;
import com.example.promotionpage.domain.faq.dao.FaqTitles;
import com.example.promotionpage.domain.faq.domain.Faq;
import com.example.promotionpage.domain.faq.dto.request.CreateFaqServiceRequestDto;
import com.example.promotionpage.domain.faq.dto.request.UpdateFaqRequestDto;
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
        if(dto.title().trim().isEmpty() || dto.content().trim().isEmpty()) {
            return ApiResponse.withError(ErrorCode.INVALID_INPUT_VALUE);
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

    public ApiResponse updateFaq(Long id, String title, String content) {
        if(title.trim().isEmpty() || content.trim().isEmpty()) {
            return ApiResponse.withError(ErrorCode.INVALID_INPUT_VALUE);
        }
        Optional<Faq> optionalFaq = faqRepository.findById(id);
        if(optionalFaq.isEmpty()) {
            return ApiResponse.withError(ErrorCode.INVALID_FAQ_ID);
        }

        Faq faq = optionalFaq.get();
        faq.updateTitle(title);
        faq.updateContent(content);

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
