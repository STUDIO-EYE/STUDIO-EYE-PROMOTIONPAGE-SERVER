package com.example.promotionpage.domain.companyInformation.application;

import com.example.promotionpage.domain.companyInformation.dao.CompanyBasicInformation;
import com.example.promotionpage.domain.companyInformation.dao.CompanyDetailInformation;
import com.example.promotionpage.domain.companyInformation.dao.CompanyInformationRepository;
import com.example.promotionpage.domain.companyInformation.dao.CompanyIntroductionInformation;
import com.example.promotionpage.domain.companyInformation.domain.CompanyInformation;
import com.example.promotionpage.domain.companyInformation.dto.request.CreateCompanyInformationServiceRequestDto;
import com.example.promotionpage.domain.faq.domain.Faq;
import com.example.promotionpage.global.adapter.S3Adapter;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyInformationService {

    private final CompanyInformationRepository companyInformationRepository;
    private final S3Adapter s3Adapter;

    public ApiResponse createCompanyInformation(CreateCompanyInformationServiceRequestDto dto,
                                                MultipartFile logoImage,
                                                MultipartFile sloganImage) throws IOException {
        String logoImageFileName = null;
        String logoImageUrl = null;
        String sloganImageFileName = null;
        String sloganImageUrl = null;
        if(logoImage != null && sloganImage != null) {
            ApiResponse<String> updateLogoFileResponse = s3Adapter.uploadFile(logoImage);
            ApiResponse<String> updateSloganFileResponse = s3Adapter.uploadFile(sloganImage);

            if (updateLogoFileResponse.getStatus().is5xxServerError() || updateSloganFileResponse.getStatus().is5xxServerError()) {
                return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
            }
            logoImageUrl = updateLogoFileResponse.getData();
            logoImageFileName = logoImage.getOriginalFilename();
            sloganImageUrl = updateSloganFileResponse.getData();
            sloganImageFileName = sloganImage.getOriginalFilename();
        }
        CompanyInformation companyInformation = dto.toEntity(logoImageFileName, logoImageUrl, sloganImageFileName, sloganImageUrl);
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("회사 정보를 성공적으로 등록하였습니다.", savedCompanyInformation);
    }

    public ApiResponse retrieveAllCampanyInformation() {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if(companyInformations.isEmpty()) {
            return ApiResponse.ok("회사 정보가 존재하지 않습니다.");
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        return ApiResponse.ok("전체 회사 정보를 성공적으로 조회하였습니다.", companyInformation);
    }

    public ApiResponse retrieveCampanyLogoImage() {
        List<String> logoImageUrls = companyInformationRepository.findLogoImageUrl();
        if(logoImageUrls.isEmpty()) {
            return ApiResponse.ok("회사 로고 이미지가 존재하지 않습니다.");
        }
        String logoImageUrl = logoImageUrls.get(0);
        return ApiResponse.ok("회사 로고 이미지를 성공적으로 조회하였습니다.", logoImageUrl);
    }

    public ApiResponse retrieveCompanyBasicInformation() {
        List<CompanyBasicInformation> companyBasicInformations = companyInformationRepository.findAddressAndPhoneAndFax();
        if(companyBasicInformations.isEmpty()) {
            return ApiResponse.ok("회사 기본 정보가 존재하지 않습니다.");
        }
        CompanyBasicInformation companyBasicInformation = companyBasicInformations.get(0);
        return ApiResponse.ok("회사 기본 정보를 성공적으로 조회하였습니다.", companyBasicInformation);
    }

    public ApiResponse retrieveCompanyIntroductionInformation() {
        List<CompanyIntroductionInformation> companyIntroductionInformations = companyInformationRepository.findIntroductionAndSloganImageUrl();
        if(companyIntroductionInformations.isEmpty()) {
            return ApiResponse.ok("회사 소개 정보가 존재하지 않습니다.");
        }
        CompanyIntroductionInformation companyIntroductionInformation = companyIntroductionInformations.get(0);
        return ApiResponse.ok("회사 소개 정보를 성공적으로 조회하였습니다.", companyIntroductionInformation);
    }

    public ApiResponse retrieveCompanyDetailInformation() {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if(companyInformations.isEmpty()) {
            return ApiResponse.ok("회사 정보가 존재하지 않습니다.");
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        Map<String, String> companyDetailInformation = companyInformation.getDetailInformation();
        if(companyDetailInformation.isEmpty()) {
            return ApiResponse.ok("회사 상세 정보가 존재하지 않습니다.");
        }
        return ApiResponse.ok("회사 상세 정보를 성공적으로 조회하였습니다.", companyDetailInformation);
    }
}
