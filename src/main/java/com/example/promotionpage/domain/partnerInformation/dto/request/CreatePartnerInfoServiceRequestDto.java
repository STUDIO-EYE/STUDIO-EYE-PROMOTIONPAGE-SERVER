package com.example.promotionpage.domain.partnerInformation.dto.request;

import com.example.promotionpage.domain.partnerInformation.domain.PartnerInformation;

public record CreatePartnerInfoServiceRequestDto(
	Boolean is_main,
	String link
) {

	public PartnerInformation toEntity(String logoImgStr) {
		return PartnerInformation.builder()
				.logoImageUrl(logoImgStr)
				.is_main(is_main)
				.link(link)
				.build();
	}

}
