package com.example.promotionpage.domain.recruitment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRecruitmentServiceRequestDto(
        Long id,
        String title,
        String period,
        String qualifications,
        String preferential,
        Boolean status
) {
}
