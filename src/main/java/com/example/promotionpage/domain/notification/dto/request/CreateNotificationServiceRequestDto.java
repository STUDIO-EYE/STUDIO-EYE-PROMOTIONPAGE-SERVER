package com.example.promotionpage.domain.notification.dto.request;

import com.example.promotionpage.domain.notification.domain.Notification;

public record CreateNotificationServiceRequestDto(
        boolean isRead
) {
    public Notification toEntity(boolean isRead) {
        return Notification.builder()
                .isRead(isRead)
                .build();
    }
}