package com.example.promotionpage.domain.companyInformation.application;

import com.example.promotionpage.domain.companyInformation.dao.CompanyBasicInformation;
import com.example.promotionpage.domain.companyInformation.dao.CompanyInformationRepository;
import com.example.promotionpage.domain.companyInformation.dao.CompanyIntroductionInformation;
import com.example.promotionpage.domain.companyInformation.domain.CompanyInformation;
import com.example.promotionpage.domain.companyInformation.dto.request.*;
import com.example.promotionpage.global.adapter.S3Adapter;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyInformationService {

    private final CompanyInformationRepository companyInformationRepository;
    private final S3Adapter s3Adapter;


    public ApiResponse<CompanyInformation> createCompanyInformation(CreateCompanyInformationServiceRequestDto dto,
                                                MultipartFile logoImage,
                                                MultipartFile sloganImage) throws IOException {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if(!companyInformations.isEmpty()) {
            return updateAllCompanyInformation(dto.toUpdateServiceRequest(), logoImage, sloganImage);
        }
        String logoImageFileName = null;
        String logoImageUrl = null;
        String sloganImageFileName = null;
        String sloganImageUrl = null;
        if(logoImage != null) {
            ApiResponse<String> updateLogoFileResponse = s3Adapter.uploadFile(logoImage);
            if (updateLogoFileResponse.getStatus().is5xxServerError()) {
                return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
            }
            logoImageUrl = updateLogoFileResponse.getData();
            logoImageFileName = logoImage.getOriginalFilename();
        }
        if(sloganImage != null) {
            ApiResponse<String> updateSloganFileResponse = s3Adapter.uploadFile(sloganImage);

            if (updateSloganFileResponse.getStatus().is5xxServerError()) {
                return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
            }
            sloganImageUrl = updateSloganFileResponse.getData();
            sloganImageFileName = sloganImage.getOriginalFilename();
        }
        CompanyInformation companyInformation = dto.toEntity(logoImageFileName, logoImageUrl, sloganImageFileName, sloganImageUrl);
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("회사 정보를 성공적으로 등록하였습니다.", savedCompanyInformation);
    }

    public ApiResponse<CompanyInformation> retrieveAllCampanyInformation() {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if(companyInformations.isEmpty()) {
            return ApiResponse.ok("회사 정보가 존재하지 않습니다.");
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        return ApiResponse.ok("전체 회사 정보를 성공적으로 조회하였습니다.", companyInformation);
    }

    public ApiResponse<String> retrieveCampanyLogoImage() {
        List<String> logoImageUrls = companyInformationRepository.findLogoImageUrl();
        if(logoImageUrls.isEmpty()) {
            return ApiResponse.ok("회사 로고 이미지가 존재하지 않습니다.");
        }
        String logoImageUrl = logoImageUrls.get(0);
        return ApiResponse.ok("회사 로고 이미지를 성공적으로 조회하였습니다.", logoImageUrl);
    }

    public ApiResponse<CompanyBasicInformation> retrieveCompanyBasicInformation() {
        List<CompanyBasicInformation> companyBasicInformations = companyInformationRepository.findAddressAndPhoneAndFax();
        if(companyBasicInformations.isEmpty()) {
            return ApiResponse.ok("회사 기본 정보가 존재하지 않습니다.");
        }
        CompanyBasicInformation companyBasicInformation = companyBasicInformations.get(0);
        return ApiResponse.ok("회사 기본 정보를 성공적으로 조회하였습니다.", companyBasicInformation);
    }

    public ApiResponse<CompanyIntroductionInformation> retrieveCompanyIntroductionInformation() {
        List<CompanyIntroductionInformation> companyIntroductionInformations = companyInformationRepository.findIntroductionAndSloganImageUrl();
        if(companyIntroductionInformations.isEmpty()) {
            return ApiResponse.ok("회사 소개 정보가 존재하지 않습니다.");
        }
        CompanyIntroductionInformation companyIntroductionInformation = companyIntroductionInformations.get(0);
        return ApiResponse.ok("회사 소개 정보를 성공적으로 조회하였습니다.", companyIntroductionInformation);
    }

    public ApiResponse<Map<String, String>> retrieveCompanyDetailInformation() {
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

    public ApiResponse<CompanyInformation> updateAllCompanyInformation(UpdateAllCompanyInformationServiceRequestDto dto,
                                                   MultipartFile logoImage,
                                                   MultipartFile sloganImage) throws IOException {

        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if (companyInformations.isEmpty()) {
            return ApiResponse.withError(ErrorCode.COMPANYINFORMATION_IS_EMPTY);
        }

        String logoImageFileName = companyInformations.get(0).getLogoImageFileName();
        String logoImageUrl = companyInformations.get(0).getLogoImageUrl();
        String sloganImageFileName = companyInformations.get(0).getSloganImageFileName();
        String sloganImageUrl = companyInformations.get(0).getSloganImageUrl();

        if(logoImage != null && !logoImage.isEmpty()) {
            if (logoImageFileName != null) s3Adapter.deleteFile(logoImageFileName);

            ApiResponse<String> updateLogoFileResponse = s3Adapter.uploadFile(logoImage);
            if (updateLogoFileResponse.getStatus().is5xxServerError()) {
                return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
            }
            logoImageUrl = updateLogoFileResponse.getData();
            logoImageFileName = logoImage.getOriginalFilename();
        }
        if(sloganImage != null && !sloganImage.isEmpty()) {
            if (sloganImageFileName != null) s3Adapter.deleteFile(sloganImageFileName);

            ApiResponse<String> updateSloganFileResponse = s3Adapter.uploadFile(sloganImage);
            if (updateSloganFileResponse.getStatus().is5xxServerError()) {
                return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
            }
            sloganImageUrl = updateSloganFileResponse.getData();
            sloganImageFileName = sloganImage.getOriginalFilename();
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        companyInformation.updateAllCompanyInformation(dto, logoImageFileName, logoImageUrl, sloganImageFileName, sloganImageUrl);
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("전체 회사 정보를 성공적으로 수정했습니다.", savedCompanyInformation);
    }

    public ApiResponse<CompanyInformation> updateAllCompanyTextInformation(UpdateAllCompanyInformationServiceRequestDto dto) throws IOException {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        CompanyInformation companyInformation = companyInformations.get(0);
        companyInformation.updateAllCompanyTextInformation(dto);
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("전체 회사 정보를 성공적으로 수정했습니다.", savedCompanyInformation);
    }

    public ApiResponse<CompanyInformation> updateCompanyBasicInformation(UpdateCompanyBasicInformationServiceRequestDto dto) {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if (companyInformations.isEmpty()) {
            ApiResponse.withError(ErrorCode.COMPANYINFORMATION_IS_EMPTY);
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        companyInformation.updateCompanyBasicInformation(dto);
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("회사 기본 정보를 성공적으로 수정했습니다.", savedCompanyInformation);
    }

    public ApiResponse<CompanyInformation> updateCompanyLogoImage(MultipartFile logoImage) throws IOException  {
        if(logoImage == null) {
            return ApiResponse.withError(ErrorCode.NOT_EXIST_IMAGE_FILE);
        }
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if (!companyInformations.isEmpty()) {
            String fileName = companyInformations.get(0).getLogoImageFileName();
            s3Adapter.deleteFile(fileName);
        }

        ApiResponse<String> updateLogoFileResponse = s3Adapter.uploadFile(logoImage);
        if (updateLogoFileResponse.getStatus().is5xxServerError()) {
            return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        companyInformation.updateCompanyLogo(logoImage.getOriginalFilename(), updateLogoFileResponse.getData());
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("회사 로고 이미지를 성공적으로 수정했습니다.", savedCompanyInformation);
    }

    public ApiResponse<CompanyInformation> updateCompanySloganImage(MultipartFile sloganImageUrl) throws IOException {
        if(sloganImageUrl == null) {
            return ApiResponse.withError(ErrorCode.NOT_EXIST_IMAGE_FILE);
        }
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if (!companyInformations.isEmpty()) {
            String fileName = companyInformations.get(0).getSloganImageFileName();
            s3Adapter.deleteFile(fileName);
        }

        ApiResponse<String> updateSloganFileResponse = s3Adapter.uploadFile(sloganImageUrl);
        if (updateSloganFileResponse.getStatus().is5xxServerError()) {
            return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        companyInformation.updateCompanySlogan(sloganImageUrl.getOriginalFilename(), updateSloganFileResponse.getData());
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("회사 슬로건 이미지를 성공적으로 수정했습니다.", savedCompanyInformation);
    }

    public ApiResponse<CompanyInformation> updateCompanyLogoAndSlogan(MultipartFile logoImage, MultipartFile sloganImage) throws IOException {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if (companyInformations.isEmpty()) {
            return ApiResponse.withError(ErrorCode.COMPANYINFORMATION_IS_EMPTY);
        }

        String logoImageFileName = companyInformations.get(0).getLogoImageFileName();
        String logoImageUrl = companyInformations.get(0).getLogoImageUrl();
        String sloganImageFileName = companyInformations.get(0).getSloganImageFileName();
        String sloganImageUrl = companyInformations.get(0).getSloganImageUrl();

        if(logoImage != null && !logoImage.isEmpty()) {
            if (logoImageFileName != null) s3Adapter.deleteFile(logoImageFileName);

            ApiResponse<String> updateLogoFileResponse = s3Adapter.uploadFile(logoImage);
            if (updateLogoFileResponse.getStatus().is5xxServerError()) {
                return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
            }
            logoImageUrl = updateLogoFileResponse.getData();
            logoImageFileName = logoImage.getOriginalFilename();
        }
        if(sloganImage != null && !sloganImage.isEmpty()) {
            if (sloganImageFileName != null) s3Adapter.deleteFile(sloganImageFileName);

            ApiResponse<String> updateSloganFileResponse = s3Adapter.uploadFile(sloganImage);
            if (updateSloganFileResponse.getStatus().is5xxServerError()) {
                return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
            }
            sloganImageUrl = updateSloganFileResponse.getData();
            sloganImageFileName = sloganImage.getOriginalFilename();
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        companyInformation.updateCompanyLogoAndSlogan(logoImageFileName, logoImageUrl, sloganImageFileName, sloganImageUrl);
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("회사 로고 이미지와 슬로건 이미지를 성공적으로 수정했습니다.", savedCompanyInformation);
    }


    public ApiResponse<CompanyInformation> updateCompanyIntroductionInformation(UpdateCompanyIntroductionInformationServiceRequestDto dto) {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        CompanyInformation companyInformation = companyInformations.get(0);
        companyInformation.updateCompanyIntroductionInformation(dto);
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("회사 소개 정보를 성공적으로 수정했습니다.", savedCompanyInformation);
    }

    public ApiResponse<CompanyInformation> updateCompanyDetailInformation(UpdateCompanyDetailInformationServiceRequestDto dto) {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if (companyInformations.isEmpty()) {
            ApiResponse.withError(ErrorCode.COMPANYINFORMATION_IS_EMPTY);
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        companyInformation.updateCompanyDetailInformation(dto);
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("회사 5가지 상세 정보를 성공적으로 수정했습니다.", savedCompanyInformation);
    }

    public ApiResponse<String> deleteAllCompanyInformation() {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if (companyInformations.isEmpty()) {
            ApiResponse.withError(ErrorCode.COMPANYINFORMATION_IS_EMPTY);
        }
        for (CompanyInformation companyInformation : companyInformations) {
            String logoFileName = companyInformation.getLogoImageFileName();
            s3Adapter.deleteFile(logoFileName);
            String sloganFileName = companyInformation.getSloganImageFileName();
            s3Adapter.deleteFile(sloganFileName);
            companyInformationRepository.delete(companyInformation);
        }
        return ApiResponse.ok("전체 회사 정보를 성공적으로 삭제했습니다.");
    }

    public ApiResponse<String> deleteCompanyLogoImage() {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if (companyInformations.isEmpty()) {
            ApiResponse.withError(ErrorCode.COMPANYINFORMATION_IS_EMPTY);
        }
        for (CompanyInformation companyInformation : companyInformations) {
            String fileName = companyInformation.getLogoImageFileName();
            s3Adapter.deleteFile(fileName);
            companyInformation.deleteLogoImage();
            companyInformationRepository.save(companyInformation);
        }
        return ApiResponse.ok("회사 로고 이미지를 성공적으로 삭제했습니다.");
    }

    public ApiResponse<CompanyInformation> deleteCompanyBasicInformation() {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if (companyInformations.isEmpty()) {
            ApiResponse.withError(ErrorCode.COMPANYINFORMATION_IS_EMPTY);
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        companyInformation.deleteCompanyBasicInformation();
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("회사 기본 정보를 성공적으로 삭제했습니다.", savedCompanyInformation);
    }

    public ApiResponse<CompanyInformation> deleteCompanyIntroductionInformation() {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if (companyInformations.isEmpty()) {
            ApiResponse.withError(ErrorCode.COMPANYINFORMATION_IS_EMPTY);
        }
        for (CompanyInformation companyInformation : companyInformations) {
            String fileName = companyInformation.getSloganImageFileName();
            s3Adapter.deleteFile(fileName);
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        companyInformation.deleteCompanyIntroductionInformation();
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("회사 소개 정보를 성공적으로 삭제했습니다.", savedCompanyInformation);
    }

    public ApiResponse<CompanyInformation> deleteCompanyDetailInformation() {
        List<CompanyInformation> companyInformations = companyInformationRepository.findAll();
        if (companyInformations.isEmpty()) {
            ApiResponse.withError(ErrorCode.COMPANYINFORMATION_IS_EMPTY);
        }
        CompanyInformation companyInformation = companyInformations.get(0);
        companyInformation.deleteCompanyDetailInformation();
        CompanyInformation savedCompanyInformation = companyInformationRepository.save(companyInformation);
        return ApiResponse.ok("회사 5가지 상세 정보를 성공적으로 삭제했습니다.", savedCompanyInformation);
    }
}
