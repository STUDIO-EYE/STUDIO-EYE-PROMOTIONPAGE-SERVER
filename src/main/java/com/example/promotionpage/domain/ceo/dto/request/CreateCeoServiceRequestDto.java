package com.example.promotionpage.domain.ceo.dto.request;

import com.example.promotionpage.domain.ceo.domain.Ceo;

import java.util.List;

public record CreateCeoServiceRequestDto(
    String name,
    String introduction
) {
    public Ceo toEntity(List<String> imageUrlList) {
        return Ceo.builder()
                .name(name)
                .introduction(introduction)
                .imageUrlList(imageUrlList)
                .build();
    }
}
