package com.example.promotionpage.domain.ceo.application;

import com.example.promotionpage.domain.ceo.dao.CeoRepository;
import com.example.promotionpage.domain.ceo.domain.Ceo;
import com.example.promotionpage.domain.ceo.dto.request.CreateCeoServiceRequestDto;
import com.example.promotionpage.domain.faq.domain.Faq;
import com.example.promotionpage.domain.partnerInformation.domain.PartnerInformation;
import com.example.promotionpage.domain.project.domain.Project;
import com.example.promotionpage.domain.project.domain.ProjectImage;
import com.example.promotionpage.domain.project.dto.request.CreateProjectServiceRequestDto;
import com.example.promotionpage.global.adapter.S3Adapter;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CeoService {
    private final CeoRepository ceoRepository;
    private final S3Adapter s3Adapter;

    public ApiResponse createCeoInformation(CreateCeoServiceRequestDto dto, List<MultipartFile> files) {
        List<String> imageUrlList = new LinkedList<>();
        if (files != null) {
            for (var file : files) {
                ApiResponse<String> updateFileResponse = s3Adapter.uploadImage(file);
                if (updateFileResponse.getStatus().is5xxServerError()) {
                    return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
                }
                String imageUrl = updateFileResponse.getData();
                if(imageUrl.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
                imageUrlList.add(imageUrl);
            }
        }
        Ceo ceo = dto.toEntity(imageUrlList);
        Ceo savedCeo = ceoRepository.save(ceo);
        return ApiResponse.ok("CEO 정보를 성공적으로 등록하였습니다.", savedCeo);
    }

    public ApiResponse retrieveCeoInformation() {
        List<Ceo> ceoList = ceoRepository.findAll();
        if(ceoList.isEmpty()) {
            return ApiResponse.ok("CEO 정보가 존재하지 않습니다.");
        }
        Ceo ceo = ceoList.get(0);
        return ApiResponse.ok("CEO 정보를 성공적으로 조회했습니다.", ceo);
    }
}
