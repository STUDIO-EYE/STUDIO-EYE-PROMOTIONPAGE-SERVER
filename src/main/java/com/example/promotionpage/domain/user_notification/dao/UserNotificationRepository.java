package com.example.promotionpage.domain.user_notification.dao;

import com.example.promotionpage.domain.user_notification.domain.UserNotification;
import com.example.promotionpage.domain.user_notification.domain.UserNotificationPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, UserNotificationPK> {
    List<UserNotification> findByUserId(Long userId);
}

