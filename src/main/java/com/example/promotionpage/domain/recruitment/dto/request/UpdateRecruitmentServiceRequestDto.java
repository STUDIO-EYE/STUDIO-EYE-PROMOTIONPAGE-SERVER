package com.example.promotionpage.domain.recruitment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateRecruitmentServiceRequestDto(
        Long id,
        String title,
        String content
) {
}
