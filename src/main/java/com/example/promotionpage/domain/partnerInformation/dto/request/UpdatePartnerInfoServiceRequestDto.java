package com.example.promotionpage.domain.partnerInformation.dto.request;

public record UpdatePartnerInfoServiceRequestDto(
	Long id,
	Boolean is_main,
	String link
) {


}
