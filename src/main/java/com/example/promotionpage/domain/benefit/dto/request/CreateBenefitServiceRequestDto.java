package com.example.promotionpage.domain.benefit.dto.request;

import com.example.promotionpage.domain.benefit.domain.Benefit;

public record CreateBenefitServiceRequestDto(
        String title,
        String content
) {
    public Benefit toEntity(String imageUrl, String imageFileName) {
        return Benefit.builder()
                .imageUrl(imageUrl)
                .imageFileName(imageFileName)
                .title(title)
                .content(content)
                .build();
    }
}
