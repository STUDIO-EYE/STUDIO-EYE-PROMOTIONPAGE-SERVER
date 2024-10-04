package com.example.promotionpage.domain.menu.dto.request;

import com.example.promotionpage.domain.menu.domain.MenuTitle;

public record CreateMenuRequestDto(
        MenuTitle menuTitle,
        Boolean visibility
) {
    public CreateMenuServiceRequestDto toServiceRequest() {
        return new CreateMenuServiceRequestDto(menuTitle, visibility);
    }
}
