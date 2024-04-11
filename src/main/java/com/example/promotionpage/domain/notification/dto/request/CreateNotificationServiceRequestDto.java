package com.example.promotionpage.domain.notification.dto.request;

import com.example.promotionpage.domain.notification.domain.Notification;

public record CreateNotificationServiceRequestDto(
        Long requestId
) {
    public Notification toEntity() {
        return Notification.builder()
                .requestId(requestId)
                .build();
    }
}