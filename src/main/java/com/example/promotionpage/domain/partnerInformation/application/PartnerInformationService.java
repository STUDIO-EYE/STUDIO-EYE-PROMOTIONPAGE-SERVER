package com.example.promotionpage.domain.partnerInformation.application;

import java.util.*;

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


	public ApiResponse createPartnerInfo(CreatePartnerInfoServiceRequestDto dto, MultipartFile logoImg) {
		String logoImgStr = getImgUrl(logoImg);
		if (logoImgStr.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);

		PartnerInformation partnerInformation = PartnerInformation.builder()
			.logoImageUrl(logoImgStr)
			.is_main(dto.is_main())
			.link(dto.link())
			.build();

		PartnerInformation savedPartnerInformation = partnerInformationRepository.save(partnerInformation);
		return ApiResponse.ok("협력사 정보를 성공적으로 등록하였습니다.", savedPartnerInformation);
	}

	private String getImgUrl(MultipartFile file) {
		ApiResponse<String> updateFileResponse = s3Adapter.uploadImage(file);

		if(updateFileResponse.getStatus().is5xxServerError()){

			return "";
		}
		String imageUrl = updateFileResponse.getData();
		return imageUrl;
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

		List<Map<String, Object>> responseList = new ArrayList<>();
		for (PartnerInformation partnerInformation : partnerInformationList) {
			Map<String, Object> responseBody = getResponseBody(partnerInformation);
			responseList.add(responseBody);
		}

		return ApiResponse.ok("협력사 정보 목록을 성공적으로 조회했습니다.", responseList);
	}

	public ApiResponse retrievePartnerInfo(Long partnerId) {
		Optional<PartnerInformation> optionalPartnerInformation = partnerInformationRepository.findById(partnerId);
		if(optionalPartnerInformation.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_PARTNER_INFORMATION_ID);
		}

		PartnerInformation partnerInformation = optionalPartnerInformation.get();
		Map<String, Object> responseBody = getResponseBody(partnerInformation);
		return ApiResponse.ok("협력사 정보를 성공적으로 조회했습니다.", responseBody);
	}

	public ApiResponse retrieveAllPartnerLogoImgList() {
		List<PartnerInformation> partnerList = partnerInformationRepository.findAll();
		if (partnerList.isEmpty()){
			return ApiResponse.ok("협력사 정보가 존재하지 않습니다.");
		}

		List<String> logoImgList = new ArrayList<>();
		for (PartnerInformation partnerInformation : partnerList) {
			logoImgList.add(partnerInformation.getLogoImageUrl());
		}

		return ApiResponse.ok("협력사 로고 이미지 리스트를 성공적으로 조회했습니다.", logoImgList);
	}


	private static Map<String, Object> getResponseBody(PartnerInformation partnerInformation) {
		// post와 get의 구조 통일
		LinkedHashMap<String, Object> responseBody = new LinkedHashMap<>();
		responseBody.put("partnerInfo", Map.of(
				"id", partnerInformation.getId(),
				"is_main", partnerInformation.getIs_main(),
				"link", partnerInformation.getLink()

		));
		responseBody.put("logoImg", partnerInformation.getLogoImageUrl());
		return responseBody;
	}

}
