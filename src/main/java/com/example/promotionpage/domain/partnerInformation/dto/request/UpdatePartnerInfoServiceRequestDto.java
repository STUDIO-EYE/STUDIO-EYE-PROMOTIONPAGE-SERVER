package com.example.promotionpage.domain.partnerInformation.dto.request;

public record UpdatePartnerInfoServiceRequestDto(
	Long id,
	String name,
	Boolean is_main,
	String link
) {


}
