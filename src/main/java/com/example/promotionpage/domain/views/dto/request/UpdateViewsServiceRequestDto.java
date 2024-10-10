package com.example.promotionpage.domain.views.dto.request;

import com.example.promotionpage.domain.menu.domain.MenuTitle;
import com.example.promotionpage.domain.project.domain.ArtworkCategory;

public record UpdateViewsServiceRequestDto(
        MenuTitle title,
        ArtworkCategory artworkCategory
) {
}
