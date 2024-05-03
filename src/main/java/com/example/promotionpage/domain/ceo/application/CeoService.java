package com.example.promotionpage.domain.ceo.application;

import com.example.promotionpage.domain.ceo.dao.CeoRepository;
import com.example.promotionpage.domain.ceo.domain.Ceo;
import com.example.promotionpage.domain.ceo.dto.request.CreateCeoServiceRequestDto;
import com.example.promotionpage.domain.ceo.dto.request.UpdateCeoServiceRequestDto;
import com.example.promotionpage.domain.companyInformation.domain.CompanyInformation;
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

    public ApiResponse createCeoInformation(CreateCeoServiceRequestDto dto, MultipartFile file) {
        List<Ceo> ceoList = ceoRepository.findAll();
        if(ceoList.size() > 0) {
            return updateCeoInformation(dto.toUpdateServiceRequest(), file);
        }
        String imageUrl = null;
        if (file != null) {
            ApiResponse<String> updateFileResponse = s3Adapter.uploadImage(file);
            if (updateFileResponse.getStatus().is5xxServerError()) {
                return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
            }
            imageUrl = updateFileResponse.getData();
            if(imageUrl.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
        }
        Ceo ceo = dto.toEntity(file.getOriginalFilename(), imageUrl);
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

    public ApiResponse updateCeoInformation(UpdateCeoServiceRequestDto dto, MultipartFile file) {
        String imageUrl = null;
        String fileName = null;
        List<Ceo> ceoList = ceoRepository.findAll();
        if(!ceoList.isEmpty()) {
            String ceoImageFileName = ceoList.get(0).getImageFileName();
            s3Adapter.deleteFile(ceoImageFileName);
        }
        if(file != null) {
            ApiResponse<String> updateFileResponse = s3Adapter.uploadImage(file);
            if (updateFileResponse.getStatus().is5xxServerError()) {
                return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
            }
            imageUrl = updateFileResponse.getData();
            if(imageUrl.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
            fileName = file.getOriginalFilename();
        }
        Ceo ceo = ceoList.get(0);
        ceo.updateCeoInformation(dto, fileName, imageUrl);
        Ceo savedCeo = ceoRepository.save(ceo);
        return ApiResponse.ok("CEO 정보를 성공적으로 수정했습니다.", savedCeo);
    }


    public ApiResponse deleteCeoInformation() {
        List<Ceo> ceoList = ceoRepository.findAll();
        if(ceoList.isEmpty()) {
            ApiResponse.withError(ErrorCode.CEO_IS_EMPTY);
        }
        Ceo ceo = ceoList.get(0);
        ceoRepository.delete(ceo);
        return ApiResponse.ok("CEO 정보를 성공적으로 삭제했습니다.");
    }
}
