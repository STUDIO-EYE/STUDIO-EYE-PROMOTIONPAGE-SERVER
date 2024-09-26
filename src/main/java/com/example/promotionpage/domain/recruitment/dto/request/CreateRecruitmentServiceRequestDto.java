package com.example.promotionpage.domain.recruitment.dto.request;

import com.example.promotionpage.domain.recruitment.domain.Recruitment;

import java.util.Date;

public record CreateRecruitmentServiceRequestDto(
        String title,
        String period,
        String qualifications,
        String preferential
) {
    public Recruitment toEntity(Date date) {
        return Recruitment.builder()
                .title(title)
                .period(period)
                .qualifications(qualifications)
                .preferential(preferential)
                .createdAt(date)
                .build();
    }
}
