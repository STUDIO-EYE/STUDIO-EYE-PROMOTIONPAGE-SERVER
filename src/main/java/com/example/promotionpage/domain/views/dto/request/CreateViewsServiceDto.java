package com.example.promotionpage.domain.views.dto.request;

import com.example.promotionpage.domain.views.domain.Views;

public record CreateViewsServiceDto(
        Integer year,
        Integer month,
        Long views
) {
    public CreateViewsServiceDto(Integer year, Integer month, Long views) {
        this.year = year;
        this.month = month;
        this.views = views;
    }
    public Views toEntity() {
        return Views.builder()
                .year(year)
                .month(month)
                .views(views)
                .build();
    }
}
