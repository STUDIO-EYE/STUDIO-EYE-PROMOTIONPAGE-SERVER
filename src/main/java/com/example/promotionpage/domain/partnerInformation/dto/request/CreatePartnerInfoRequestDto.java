package com.example.promotionpage.domain.partnerInformation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePartnerInfoRequestDto(
	@NotNull(message = "is_main은 필수 값입니다.")
	Boolean is_main,

	@Schema(description = "협력사 링크 URL, null / 빈 값 / 공백 허용 x")
	@NotBlank(message = "link는 필수 값입니다.")
	String link
) {

	public CreatePartnerInfoServiceRequestDto toServiceRequest() {
		return new CreatePartnerInfoServiceRequestDto( is_main, link);
	}
}
