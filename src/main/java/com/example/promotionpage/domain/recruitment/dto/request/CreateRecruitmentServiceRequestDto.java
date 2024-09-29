package com.example.promotionpage.domain.recruitment.dto.request;

import com.example.promotionpage.domain.recruitment.domain.Recruitment;

import java.util.Date;

public record CreateRecruitmentServiceRequestDto(
        String title,
        Date startDate,
        Date deadline,
        String qualifications,
        String preferential,
        String link
) {
    public Recruitment toEntity(Date date) {
        return Recruitment.builder()
                .title(title)
                .startDate(startDate)
                .deadline(deadline)
                .qualifications(qualifications)
                .preferential(preferential)
                .link(link)
                .createdAt(date)
                .build();
    }
}
