package com.example.promotionpage.domain.request.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RetrieveRequestCountDto(
        @Schema(description = "시작연도, 빈 값/공백/null 을 허용하지 않습니다.")
        @NotBlank(message = "startYear는 필수 값입니다.")
        Integer startYear,

        @Schema(description = "시작월, 빈 값/공백/null 을 허용하지 않습니다.")
        @NotBlank(message = "startMonth는 필수 값입니다.")
        Integer startMonth,

        @Schema(description = "끝연도, 빈 값/공백/null 을 허용하지 않습니다.")
        @NotBlank(message = "endYear은 필수 값입니다.")
        Integer endYear,

        @Schema(description = "끝월, 빈 값/공백/null 을 허용하지 않습니다.")
        @NotBlank(message = "endMonth은 필수 값입니다.")
        Integer endMonth
) {
    public RetrieveRequestCountServiceDto toServiceRequest() {
        return new RetrieveRequestCountServiceDto(startYear, startMonth, endYear, endMonth);
    }
}
