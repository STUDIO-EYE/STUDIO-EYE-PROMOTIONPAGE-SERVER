package com.example.promotionpage.domain.benefit.application;

import com.example.promotionpage.domain.benefit.dao.BenefitRepository;
import com.example.promotionpage.domain.benefit.domain.Benefit;
import com.example.promotionpage.domain.benefit.dto.request.CreateBenefitServiceRequestDto;
import com.example.promotionpage.global.adapter.S3Adapter;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BenefitService {
    private final BenefitRepository benefitRepository;
    private final S3Adapter s3Adapter;

    public ApiResponse<Benefit> createBenefit(CreateBenefitServiceRequestDto dto, MultipartFile file) {
        if(file.isEmpty()) {
            return ApiResponse.withError(ErrorCode.NOT_EXIST_IMAGE_FILE);
        }
        ApiResponse<String> updateFileResponse = s3Adapter.uploadImage(file);
        if (updateFileResponse.getStatus().is5xxServerError()) {
            return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
        }
        String imageUrl = updateFileResponse.getData();
        String imageFileName = file.getOriginalFilename();
        Benefit benefit = dto.toEntity(imageUrl, imageFileName);
        Benefit savedBenefit = benefitRepository.save(benefit);
        return ApiResponse.ok("혜택 정보를 성공적으로 등록하였습니다.", savedBenefit);
    }

    public ApiResponse<List<Benefit>> retrieveBenefit() {
        List<Benefit> benefits = benefitRepository.findAll();
        if(benefits.isEmpty()) {
            return ApiResponse.ok("혜택 정보가 존재하지 않습니다.");
        }
        return ApiResponse.ok("혜택 정보를 성공적으로 조회했습니다.", benefits);
    }
}
