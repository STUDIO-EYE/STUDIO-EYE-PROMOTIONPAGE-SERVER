package com.example.promotionpage.domain.userNotification.application;

import com.example.promotionpage.domain.notification.dao.NotificationRepository;
import com.example.promotionpage.domain.notification.domain.Notification;
import com.example.promotionpage.domain.userNotification.dao.UserNotificationRepository;
import com.example.promotionpage.domain.userNotification.domain.UserNotification;
import com.example.promotionpage.domain.userNotification.domain.UserNotificationPK;
import com.example.promotionpage.domain.userNotification.dto.request.CreateUserNotificationServiceRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserNotificationService {

    private static final Boolean READ = true;
    private static final Boolean UNREAD = false;

    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;

    public ApiResponse<UserNotification> createUserNotification(Long userId, Long notificationId) {
        UserNotification userNotification =
                new CreateUserNotificationServiceRequestDto(userId, notificationId, UNREAD).toEntity();
        userNotificationRepository.save(userNotification);
        return ApiResponse.ok("USER_NOTIFICATION 정보를 저장하였습니다.", userNotification);
    }

    public ApiResponse<List<Map<String, Object>>> retrieveAllUserNotification(Long userId) {
        List<UserNotification> userNotificationList = userNotificationRepository.findByUserId(userId);
        List<Map<String, Object>> notificationDetails = new ArrayList<>();

        for (UserNotification userNotification : userNotificationList) {
            Long notificationId = userNotification.getNotificationId();
            Optional<Notification> notificationDetail = notificationRepository.findById(notificationId);
            notificationDetail.ifPresent(notification -> {
                Map<String, Object> details = new HashMap<>();
                details.put("notification", notification);
                details.put("isRead", userNotification.getIsRead());
                notificationDetails.add(details);
            });
        }
        if(notificationDetails.isEmpty()) {
            return ApiResponse.ok("유저의 알림이 존재하지 않습니다.");
        }

        return ApiResponse.ok("유저의 알림 목록을 성공적으로 조회했습니다.", notificationDetails);
    }

    public ApiResponse<UserNotification> checkNotification(Long userId, Long notificationId) {
        UserNotificationPK userNotificationPK = new UserNotificationPK(userId, notificationId);
        Optional<UserNotification> userNotification = userNotificationRepository.findById(userNotificationPK);
        if (userNotification.isPresent()) {
            userNotification.get().updateIsRead(READ);
            UserNotification checkedUserNotification = userNotificationRepository.save(userNotification.get());
            return ApiResponse.ok("알림을 성공적으로 읽음 처리했습니다.", checkedUserNotification);
        } else {
            return ApiResponse.withError(ErrorCode.INVALID_USER_NOTIFICATION_ID);
        }
    }

    public ApiResponse<Optional<UserNotification>> deleteUserNotification(Long userId, Long notificationId) {
        try {
            UserNotificationPK userNotificationPK = new UserNotificationPK(userId, notificationId);
            Optional<UserNotification> userNotification = userNotificationRepository.findById(userNotificationPK);
            // UserNotification이 존재하는 경우 삭제
            if (userNotification.isPresent()) {
                userNotificationRepository.delete(userNotification.get());
                return ApiResponse.ok("알림을 성공적으로 삭제하였습니다.", userNotification);
            } else {
                return ApiResponse.withError(ErrorCode.INVALID_USER_NOTIFICATION_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.withError(ErrorCode.FAILED_USER_NOTIFICATION_DELETE);
        }
    }
}
