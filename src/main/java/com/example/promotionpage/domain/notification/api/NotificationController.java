package com.example.promotionpage.domain.notification.api;

import com.example.promotionpage.domain.notification.application.NotificationService;
import com.example.promotionpage.domain.notification.dto.request.CreateNotificationRequestDto;
import com.example.promotionpage.domain.user.service.UserServiceImpl;
import com.example.promotionpage.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Tag(name = "알림 API", description = "알림 등록 / 조회")
@RestController
// TODO 추후 /api를 /admin으로 변경해야 한다.
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    // 당장은 필요없는 api
    @Operation(summary = "알림 등록 API")
    @PostMapping("/notification/{requestId}")
    public ApiResponse sendData(Long requestId) {
        return notificationService.sendNotification(requestId);
    }

    @Operation(summary = "알림 전체 조회 API")
    @GetMapping("/notification")
    public ApiResponse retrieveAllNotification(){
        return notificationService.retrieveAllNotification();
    }

    @Operation(summary = "알림 수정(읽음 처리) API")
    @PutMapping("/notification/{notificationId}")
    public ApiResponse updateNotification(@PathVariable Long notificationId){
        return notificationService.updateNotification(notificationId);
    }

    @Operation(summary = "알림 삭제 API")
    @DeleteMapping("/notification/{notificationId}")
    public ApiResponse deleteNotification(@PathVariable Long notificationId) throws IOException {
        return notificationService.deleteNotification(notificationId);
    }


//    @Operation(summary = "알림 등록(직접 등록) API")
//    @PostMapping("/notification")
//    public ApiResponse createNotification(@Valid @RequestBody CreateNotificationRequestDto dto){
//        return notificationService.createNotification(dto.toServiceRequest());
//    }
//    @Operation(summary = "알림 등록(initial; unread) API")
//    @PostMapping("/notification/initial")
//    public ApiResponse createNotification(Long requestId){
//        return notificationService.justCreateNotification(requestId);
//    }
//

//
//    @Operation(summary = "알림 수정(읽음 처리) API")
//    @PutMapping("/notification/{notificationId}")
//    public ApiResponse updateNotification(@PathVariable Long notificationId){
//        return notificationService.updateNotification(notificationId);
//    }
}
