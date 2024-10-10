package com.example.promotionpage.domain.views.dto.request;

import com.example.promotionpage.domain.menu.domain.MenuTitle;
import com.example.promotionpage.domain.project.domain.ArtworkCategory;

public record UpdateViewsRequestDto(
        MenuTitle menu,
        ArtworkCategory artworkCategory
) {
    public UpdateViewsServiceRequestDto toServiceRequest() {
        return new UpdateViewsServiceRequestDto(menu, artworkCategory);
    }
}
