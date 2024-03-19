package com.example.promotionpage.domain.noticeBoard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateNoticeBoardRequestDto(

	@Schema(description = "공지 사항 제목, 백엔드에서 @Valid를 통한 유효성(빈 값, 공백, null) 검사가 안되니, 프론트에서 처리해주세요.")
	@NotBlank(message = "공지 사항의 제목은 필수 값입니다.")
	String title
) {
}