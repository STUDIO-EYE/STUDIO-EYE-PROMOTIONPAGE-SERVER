package com.example.promotionpage.domain.menu.dto.request;

import com.example.promotionpage.domain.menu.domain.Menu;

public record CreateMenuServiceRequestDto(
        String title,
        Boolean visibility
) {
    public Menu toEntity() {
        return Menu.builder()
                .title(title)
                .visibility(visibility)
                .build();
    }
}
