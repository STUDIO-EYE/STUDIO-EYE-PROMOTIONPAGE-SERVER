package com.example.promotionpage.domain.notification.application;


import com.example.promotionpage.domain.notification.dao.EmitterRepository;
import com.example.promotionpage.domain.notification.dao.NotificationRepository;
import com.example.promotionpage.domain.notification.domain.Notification;
import com.example.promotionpage.domain.notification.dto.request.CreateNotificationServiceRequestDto;
import com.example.promotionpage.domain.user.dto.GetUserResponseDto;
import com.example.promotionpage.domain.user.service.UserServiceImpl;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserServiceImpl userServiceImpl;
    private final Boolean READ = true;
    private final Boolean UNREAD = false;

    private final EmitterRepository emitterRepository;

    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 600L * 1000 * 60;

    public ApiResponse subscribe(Long requestId) {
        System.out.println("subscribe");
        // 모든 유저 가져오기
        List<Long> userIds = userServiceImpl.getAllApprovedUserIds();
        if (userIds.isEmpty()) {
            return ApiResponse.withError(ErrorCode.USER_IS_EMPTY);
        } else {
            System.out.println(userIds);
            for (Long userId : userIds) {
                SseEmitter emitter = createEmitter(userId);
                System.out.println(emitter);
                emitterRepository.save(userId, emitter);
                sendNotification(requestId);

                // Emitter가 완료될 때(모든 데이터가 성공적으로 전송되었을 때) Emitter를 삭제한다.
                emitter.onCompletion(() -> emitterRepository.deleteById(userId));
                // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
                emitter.onTimeout(() -> emitterRepository.deleteById(userId));
            }

            return ApiResponse.ok("알림을 성공적으로 구독하였습니다.", requestId);
        }
    }

    public ApiResponse sendNotification(Long requestId) {
        Notification notification = new CreateNotificationServiceRequestDto(UNREAD, requestId).toEntity();
        Notification savedNotification = notificationRepository.save(notification);

        Collection<SseEmitter> emitters = emitterRepository.getAllEmitters();
        for (SseEmitter emitter : emitters) {
            if (emitter != null) {
                try {
                    emitter.send(savedNotification);
                } catch (IOException e) {
                    emitter.completeWithError(e);
                    return ApiResponse.withError(ErrorCode.INVALID_SSE_ID);
                }
            }
        }
        return ApiResponse.ok("알림을 성공적으로 등록하였습니다.", savedNotification);
    }

    public ApiResponse retrieveAllNotification() {
        List<Notification> notificationList = notificationRepository.findAll();
        if(notificationList.isEmpty()) {
            return ApiResponse.ok("알림이 존재하지 않습니다.");
        }
        return ApiResponse.ok("알림 목록을 성공적으로 조회했습니다.", notificationList);
    }

    public ApiResponse deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new IllegalArgumentException("알림을 찾을 수 없습니다.")
        );

        notificationRepository.delete(notification);
        return ApiResponse.ok("알림을 성공적으로 삭제하였습니다.", notification);
    }

    public ApiResponse updateNotification(Long notificationId) {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if(optionalNotification.isEmpty()){
            return ApiResponse.withError(ErrorCode.INVALID_NOTIFICATION_ID);
        }
        Notification notification = optionalNotification.get();
        if(notification.getIsRead().equals(READ)) {
            return ApiResponse.ok("이미 읽은 알림입니다.", notification);
        }
        notification.updateIsChecked(READ);
        Notification updatedNotification = notificationRepository.save(notification);
        return ApiResponse.ok("알림을 성공적으로 수정(읽음처리)했습니다.", updatedNotification);
    }

    private SseEmitter createEmitter(Long id) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(id, emitter);

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송되었을 때) Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        return emitter;
    }

}
