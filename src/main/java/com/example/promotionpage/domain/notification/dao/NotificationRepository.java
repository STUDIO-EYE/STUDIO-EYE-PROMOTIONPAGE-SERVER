package com.example.promotionpage.domain.notification.dao;

import com.example.promotionpage.domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findById(Long id);
}
