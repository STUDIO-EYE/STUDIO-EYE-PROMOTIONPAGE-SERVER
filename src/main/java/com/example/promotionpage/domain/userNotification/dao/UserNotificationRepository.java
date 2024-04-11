package com.example.promotionpage.domain.userNotification.dao;

import com.example.promotionpage.domain.userNotification.domain.UserNotification;
import com.example.promotionpage.domain.userNotification.domain.UserNotificationPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, UserNotificationPK> {
    List<UserNotification> findByUserId(Long userId);;
}

