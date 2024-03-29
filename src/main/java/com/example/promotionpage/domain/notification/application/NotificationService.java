package com.example.promotionpage.domain.notification.application;


import com.example.promotionpage.domain.notification.dao.NotificationRepository;
import com.example.promotionpage.domain.notification.domain.Notification;
import com.example.promotionpage.domain.notification.dto.request.CreateNotificationServiceRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(CreateNotificationServiceRequestDto dto) {
        Notification notification = dto.toEntity(false);
        notificationRepository.save(notification);
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
