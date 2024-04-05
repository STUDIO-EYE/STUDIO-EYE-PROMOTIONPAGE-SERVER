package com.example.promotionpage.domain.faq.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateFaqRequestDto (
    @Schema(description = "FAQ 제목, 빈 값/공백/null 을 허용하지 않습니다.")
    @NotBlank(message = "FAQ 제목은 필수 값입니다.")
    String title,
    @Schema(description = "FAQ 내용, 빈 값/공백/null 을 허용하지 않습니다.")
    @NotBlank(message = "FAQ 내용은 필수 값입니다.")
    String content
) {
    public CreateFaqServiceRequestDto toServiceFaq() {
        return new CreateFaqServiceRequestDto(title, content);
    }
}
