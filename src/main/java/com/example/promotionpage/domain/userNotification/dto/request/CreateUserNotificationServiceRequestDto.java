package com.example.promotionpage.domain.userNotification.dto.request;

import com.example.promotionpage.domain.userNotification.domain.UserNotification;
import com.example.promotionpage.domain.userNotification.domain.UserNotificationPK;

public record CreateUserNotificationServiceRequestDto(
        Long userId,
        Long notificationId,
        Boolean isRead
) {
        public UserNotification toEntity() {
            return UserNotification.builder()
                    .userId(userId)
                    .notificationId(notificationId)
                    .isRead(isRead)
                    .build();
        }
}
