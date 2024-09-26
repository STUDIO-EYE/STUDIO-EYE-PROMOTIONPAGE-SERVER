package com.example.promotionpage.domain.recruitment.application;

import com.example.promotionpage.domain.recruitment.dao.RecruitmentRepository;
import com.example.promotionpage.domain.recruitment.dao.RecruitmentTitle;
import com.example.promotionpage.domain.recruitment.domain.Recruitment;
import com.example.promotionpage.domain.recruitment.dto.request.CreateRecruitmentServiceRequestDto;
import com.example.promotionpage.domain.recruitment.dto.request.UpdateRecruitmentServiceRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    public ApiResponse<Recruitment> createRecruitment(CreateRecruitmentServiceRequestDto dto) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Recruitment recruitment = dto.toEntity(new Date());

        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

        return ApiResponse.ok("채용공고 게시물을 성공적으로 등록하였습니다.", savedRecruitment);
    }

    public ApiResponse<Page<RecruitmentTitle>> retrieveRecruitmentList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecruitmentTitle> recruitmentTitleList = recruitmentRepository.findAllRecruitments(pageable);
        return ApiResponse.ok("채용공고 목록을 성공적으로 조회했습니다.", recruitmentTitleList);
    }

    public ApiResponse<Recruitment> retrieveRecruitmentById(Long id) {
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(id);
        if(optionalRecruitment.isEmpty()) {
            return ApiResponse.withError(ErrorCode.INVALID_RECRUITMENT_ID);
        }
        Recruitment recruitment = optionalRecruitment.get();
        return ApiResponse.ok("채용공고를 성공적으로 조회했습니다.", recruitment);
    }

    public ApiResponse<Recruitment> retrieveRecentRecruitment() {
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findTopByOrderByCreatedAtDesc();
        if(optionalRecruitment.isEmpty()) {
            return ApiResponse.ok(ErrorCode.RECRUITMENT_IS_EMPTY.getMessage());
//            return ApiResponse.withError(ErrorCode.INVALID_RECRUITMENT_ID);
        }
        Recruitment recruitment = optionalRecruitment.get();
        return ApiResponse.ok("가장 최근 채용공고를 성공적으로 조회했습니다.", recruitment);
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
