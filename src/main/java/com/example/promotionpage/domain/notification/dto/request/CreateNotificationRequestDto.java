package com.example.promotionpage.domain.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateNotificationRequestDto(
        @Schema(description = "읽음여부, 빈 값/공백/null 을 허용하지 않습니다.")
        @NotBlank(message = "읽음여부은 필수 값입니다.")
        Boolean isRead
) {

    public CreateNotificationServiceRequestDto toServiceRequest() {
        return new CreateNotificationServiceRequestDto(isRead);
    }
}