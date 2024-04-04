package com.example.promotionpage.domain.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateNotificationRequestDto(
        @Schema(description = "읽음여부, 빈 값/공백/null 을 허용하지 않습니다.")
        @NotBlank(message = "읽음여부은 필수 값입니다.")
        Boolean isRead,
        @Schema(description = "문의 ID, 빈 값/공백/null 을 허용하지 않습니다.")
        @NotBlank(message = "문의 ID는 필수 값입니다.")
        Long requestId
) {

    public CreateNotificationServiceRequestDto toServiceRequest() {
        return new CreateNotificationServiceRequestDto(isRead, requestId);
    }
}