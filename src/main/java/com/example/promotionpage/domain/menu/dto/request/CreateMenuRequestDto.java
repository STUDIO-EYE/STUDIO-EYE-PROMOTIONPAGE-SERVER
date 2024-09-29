package com.example.promotionpage.domain.menu.dto.request;

public record CreateMenuRequestDto(
        String title,
        Boolean visibility
) {
    public CreateMenuServiceRequestDto toServiceRequest() {
        return new CreateMenuServiceRequestDto(title, visibility);
    }
}
