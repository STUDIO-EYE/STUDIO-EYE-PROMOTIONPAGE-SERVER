package com.example.promotionpage.domain.menu.dto.request;

public record UpdateMenuRequestDto(
        Long id,
        String title,
        Boolean visibility
) {
    public UpdateMenuServiceRequestDto toServiceRequest() {
        return new UpdateMenuServiceRequestDto(id, title, visibility);
    }
}
