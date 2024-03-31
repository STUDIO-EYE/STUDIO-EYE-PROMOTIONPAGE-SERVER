package com.example.promotionpage.domain.notification.dao;

import com.example.promotionpage.domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
