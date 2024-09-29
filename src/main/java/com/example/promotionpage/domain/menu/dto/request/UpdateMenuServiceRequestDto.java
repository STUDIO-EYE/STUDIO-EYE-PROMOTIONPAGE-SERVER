package com.example.promotionpage.domain.menu.dto.request;

public record UpdateMenuServiceRequestDto(
        Long id,
        String title,
        Boolean visibility
) {
}
