package com.example.promotionpage.domain.ceo.dto.request;

import com.example.promotionpage.domain.ceo.domain.Ceo;

import java.util.List;

public record UpdateCeoServiceRequestDto(
    String name,
    String introduction
) {
    public Ceo toEntity(String imageFileName, String imageUrl) {
        return Ceo.builder()
                .name(name)
                .introduction(introduction)
                .imageFileName(imageFileName)
                .imageUrl(imageUrl)
                .build();
    }
}
