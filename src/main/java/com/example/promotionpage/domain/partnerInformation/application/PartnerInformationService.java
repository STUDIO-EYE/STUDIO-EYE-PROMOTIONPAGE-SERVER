package com.example.promotionpage.domain.partnerInformation.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.promotionpage.domain.partnerInformation.dao.PartnerInformationRepository;
import com.example.promotionpage.domain.partnerInformation.domain.PartnerInformation;
import com.example.promotionpage.domain.partnerInformation.dto.request.CreatePartnerInfoServiceRequestDto;
import com.example.promotionpage.global.adapter.S3Adapter;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerInformationService {

	private final PartnerInformationRepository partnerInformationRepository;
	private final S3Adapter s3Adapter;


	public ApiResponse createPartnerInfo(CreatePartnerInfoServiceRequestDto dto, MultipartFile file) {
		ApiResponse<String> updateFileResponse = s3Adapter.uploadImage(file);
		if(updateFileResponse.getStatus().is5xxServerError()){
			return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
		}
		String imageUrl = updateFileResponse.getData();

		PartnerInformation partnerInformation = PartnerInformation.builder()
			.logoImageUrl(imageUrl)
			.is_main(dto.is_main())
			.link(dto.link())
			.build();

		PartnerInformation savedPartnerInformation = partnerInformationRepository.save(partnerInformation);
		return ApiResponse.ok("협력사 정보를 성공적으로 등록하였습니다.", savedPartnerInformation);
	}

	public ApiResponse deletePartnerInfo(Long partnerId) {
		Optional<PartnerInformation> optionalPartnerInformation = partnerInformationRepository.findById(partnerId);
		if(optionalPartnerInformation.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_PARTNER_INFORMATION_ID);
		}

		PartnerInformation partnerInformation = optionalPartnerInformation.get();
		ApiResponse<String> deleteFileResponse = s3Adapter.deleteFile(partnerInformation.getLogoImageUrl().split("/")[3]);
		if(deleteFileResponse.getStatus().is5xxServerError()){
			return ApiResponse.withError(ErrorCode.ERROR_S3_DELETE_OBJECT);
		}

		partnerInformationRepository.delete(partnerInformation);
		return ApiResponse.ok("협력사 정보를 성공적으로 삭제하였습니다.");
	}

	public ApiResponse retrieveAllPartnerInfo() {
		List<PartnerInformation> partnerInformationList = partnerInformationRepository.findAll();
		if (partnerInformationList.isEmpty()){
			return ApiResponse.ok("협력사 정보가 존재하지 않습니다.");
		}

		return ApiResponse.ok("협력사 정보 목록을 성공적으로 조회했습니다.", partnerInformationList);
	}

	public ApiResponse retrievePartnerInfo(Long partnerId) {
		Optional<PartnerInformation> optionalPartnerInformation = partnerInformationRepository.findById(partnerId);
		if(optionalPartnerInformation.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_PARTNER_INFORMATION_ID);
		}

		PartnerInformation partnerInformation = optionalPartnerInformation.get();
		return ApiResponse.ok("협력사 정보를 성공적으로 조회했습니다.", partnerInformation);
	}
}
