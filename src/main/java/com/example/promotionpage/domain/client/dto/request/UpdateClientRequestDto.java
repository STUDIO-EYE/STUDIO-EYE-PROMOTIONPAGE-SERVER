package com.example.promotionpage.domain.client.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateClientRequestDto(
    @Schema(description = "클라이언트 식별자")
    @Positive(message = "클라이언트 식별자는 양수여야 합니다.")
    Long clientId,

    @Schema(description = "클라이언트 이름, 빈 값/공백/null 을 허용하지 않습니다.")
    @NotBlank(message = "클라이언트 이름은 필수 값입니다.")
    String name
) {
    public UpdateClientServiceRequestDto toServiceRequest() {
        return new UpdateClientServiceRequestDto(clientId, name);
    }
}
