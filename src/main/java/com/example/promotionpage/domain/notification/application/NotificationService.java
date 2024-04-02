package com.example.promotionpage.domain.notification.application;


import com.example.promotionpage.domain.notification.dao.NotificationRepository;
import com.example.promotionpage.domain.notification.domain.Notification;
import com.example.promotionpage.domain.notification.dto.request.CreateNotificationServiceRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final Boolean READ = true;
    private final Boolean UNREAD = false;

    public ApiResponse createNotification(CreateNotificationServiceRequestDto dto) { // test용 알림 직접 등록
        Notification notification = dto.toEntity();
        Notification savedNotification = notificationRepository.save(notification);
        return ApiResponse.ok("알림을 성공적으로 등록하였습니다.", savedNotification);
    }
    public ApiResponse justCreateNotification(Long requestId) { // 기본 알림 등록
        Notification notification = new CreateNotificationServiceRequestDto(UNREAD, requestId).toEntity();
        Notification savedNotification = notificationRepository.save(notification);
        return ApiResponse.ok("알림을 성공적으로 등록하였습니다.", savedNotification);
    }

    public ApiResponse retrieveAllNotification() {
        List<Notification> notificationList = notificationRepository.findAll();
        if(notificationList.isEmpty()) {
            return ApiResponse.ok("알림이 존재하지 않습니다.");
        }
        return ApiResponse.ok("알림 목록을 성공적으로 조회했습니다.", notificationList);
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

//
//    private final EmitterRepository emitterRepository;
//
//    // 기본 타임아웃 설정
//    private static final Long DEFAULT_TIMEOUT = 600L * 1000 * 60;
//
//    public SseEmitter subscribe(Long userId) {
//        SseEmitter emitter = createEmitter(userId);
//
//        sendNotification(userId, "EventStream Created. [userId=" + userId + "]");
//        return emitter;
//    }
//
//    public void sendNotification(Long id, Object data) {
//        SseEmitter emitter = emitterRepository.get(id);
//        if (emitter != null) {
//            try {
//                emitter.send(SseEmitter.event().id(String.valueOf(id)).name("sse").data(data));
//            } catch (IOException e) {
//                emitterRepository.deleteById(id);
//                emitter.completeWithError(e);            }
//        }
//    }
//
//    private SseEmitter createEmitter(Long id) {
//        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
//        emitterRepository.save(id, emitter);
//
//        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송되었을 때) Emitter를 삭제한다.
//        emitter.onCompletion(() -> emitterRepository.deleteById(id));
//        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
//        emitter.onTimeout(() -> emitterRepository.deleteById(id));
//
//        return emitter;
//    }
}
