package com.example.promotionpage.domain.recruitment.application;

import com.example.promotionpage.domain.recruitment.dao.RecruitmentRepository;
import com.example.promotionpage.domain.recruitment.domain.Recruitment;
import com.example.promotionpage.domain.recruitment.dto.request.CreateRecruitmentServiceRequestDto;
import com.example.promotionpage.domain.recruitment.dto.request.UpdateRecruitmentServiceRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    public ApiResponse<Recruitment> createRecruitment(CreateRecruitmentServiceRequestDto dto) {
        Recruitment recruitment = dto.toEntity();

        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

        return ApiResponse.ok("채용공고 게시물을 성공적으로 등록하였습니다.", savedRecruitment);
    }

    public ApiResponse<Recruitment> retrieveRecruitmentById(Long id) {
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(id);
        if(optionalRecruitment.isEmpty()) {
            return ApiResponse.withError(ErrorCode.INVALID_RECRUITMENT_ID);
        }
        Recruitment recruitment = optionalRecruitment.get();
        return ApiResponse.ok("채용공고를 성공적으로 조회했습니다.", recruitment);
    }

    public ApiResponse<Recruitment> updateRecruitment(UpdateRecruitmentServiceRequestDto dto) {
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(dto.id());
        if(optionalRecruitment.isEmpty()) {
            return ApiResponse.withError(ErrorCode.INVALID_RECRUITMENT_ID);
        }
        Recruitment recruitment = optionalRecruitment.get();
        recruitment.update(dto);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);
        return ApiResponse.ok("채용공고 게시물을 성공적으로 수정했습니다.", savedRecruitment);
    }

    public ApiResponse<String> deleteRecruitment(Long id) {
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(id);
        if(optionalRecruitment.isEmpty()) {
            return ApiResponse.withError(ErrorCode.INVALID_RECRUITMENT_ID);
        }
        Recruitment recruitment = optionalRecruitment.get();
        recruitmentRepository.delete(recruitment);
        return ApiResponse.ok("채용공고를 성공적으로 삭제하였습니다.");
    }
}
