package com.example.promotionpage.domain.request.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateRequestCommentDto(
        @Schema(description = "답변, 빈 값/공백/null 을 허용하지 않습니다.")
        @NotBlank(message = "답변은 필수 값입니다.")
        String answer
) {
    public UpdateRequestCommentServiceDto toServiceRequest() {
        return new UpdateRequestCommentServiceDto(answer);
    }
}
