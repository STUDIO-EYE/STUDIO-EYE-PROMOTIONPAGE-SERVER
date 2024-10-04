package com.example.promotionpage.domain.menu.dto.request;

import com.example.promotionpage.domain.menu.domain.Menu;
import com.example.promotionpage.domain.menu.domain.MenuTitle;

public record CreateMenuServiceRequestDto(
        MenuTitle menuTitle,
        Boolean visibility
) {
    public Menu toEntity(int totalCount) {
        return Menu.builder()
                .menuTitle(menuTitle)
                .visibility(visibility)
                .sequence(totalCount)
                .build();
    }
}
