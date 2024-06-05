package com.example.promotionpage.domain.userNotification.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserNotificationPK implements Serializable {
    private Long userId;
    private Long notificationId;

    public UserNotificationPK(Long userId, Long notificationId) {
        this.userId = userId;
        this.notificationId = notificationId;
    }
}
