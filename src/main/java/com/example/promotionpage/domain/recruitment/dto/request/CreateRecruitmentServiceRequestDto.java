package com.example.promotionpage.domain.recruitment.dto.request;

import com.example.promotionpage.domain.recruitment.domain.Recruitment;

public record CreateRecruitmentServiceRequestDto(
        String title,
        String content
) {
    public Recruitment toEntity() {
        return Recruitment.builder()
                .title(title)
                .content(content)
                .build();
    }
}
