package com.example.promotionpage.domain.noticeBoard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateNoticeBoardRequestDto(

	@Schema(description = "공지 사항 제목, 백엔드에서 @Valid를 통한 유효성(빈 값, 공백, null) 검사가 안되니, 프론트에서 처리해주세요.")
	@NotBlank(message = "공지 사항의 제목은 필수 값입니다.")
	String title,

	@Schema(description = "공지 사항 식별자 (0 이하의 값 허용 x), 백엔드에서 @Valid를 통한 유효성(빈 값, 공백, null) 검사가 안되니, 프론트에서 처리해주세요.")
	@Positive(message = "공지 사항 식별자는 양수여야 합니다.")
	Long noticeBoardId
) {
}
