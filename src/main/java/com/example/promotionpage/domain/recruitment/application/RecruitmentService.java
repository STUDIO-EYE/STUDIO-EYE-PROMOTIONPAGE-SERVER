package com.example.promotionpage.domain.recruitment.application;

import com.example.promotionpage.domain.recruitment.dao.RecruitmentRepository;
import com.example.promotionpage.domain.recruitment.domain.Recruitment;
import com.example.promotionpage.domain.recruitment.dto.request.CreateRecruitmentServiceRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
