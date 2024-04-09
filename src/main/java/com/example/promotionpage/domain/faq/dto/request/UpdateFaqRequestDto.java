package com.example.promotionpage.domain.faq.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateFaqRequestDto (
        @Schema(description = "FAQ 식별자 (0 이하의 값 허용 x), 백엔드에서 @Valid를 통한 유효성(빈 값, 공백, null) 검사가 안되니, 프론트에서 처리해주세요.")
        @NotBlank(message = "FAQ 식별자는 양수여야 합니다.")
        Long id,
        @Schema(description = "FAQ 제목, 빈 값/공백/null 을 허용하지 않습니다.")
        @NotBlank(message = "FAQ 제목은 필수 값입니다.")
        String title,
        @Schema(description = "FAQ 내용, 빈 값/공백/null 을 허용하지 않습니다.")
        @NotBlank(message = "FAQ 내용은 필수 값입니다.")
        String content
) {
}
