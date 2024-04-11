package com.example.promotionpage.domain.userNotification.api;

import com.example.promotionpage.domain.userNotification.application.UserNotificationService;
import com.example.promotionpage.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Tag(name = "특정 유저 알림 API", description = "유저 알림 조회")
@RestController
// TODO 추후 /api를 /admin으로 변경해야 한다.
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserNotificationController {

    private final UserNotificationService userNotificationService;

    @Operation(summary = "특정 유저에게 알림 등록 (TEST) API")
    @PostMapping("/userNotification/{userId}/{notificationId}")
    public ApiResponse createUserNotificationTEST(@RequestParam Long userId, @PathVariable Long notificationId) {
        return userNotificationService.createUserNotification(userId, notificationId);
    }

    @Operation(summary = "특정 유저의 알림 전체 조회 API")
    @GetMapping("/userNotification/{userId}")
    public ApiResponse retrieveAllNotification(@PathVariable Long userId) {
        return userNotificationService.retrieveAllUserNotification(userId);
    }

    @Operation(summary = "알림 수정(읽음 처리) API")
    @PutMapping("/userNotification/{userId}/{notificationId}")
    public ApiResponse checkNotification(@RequestParam Long userId, @PathVariable Long notificationId) {
        return userNotificationService.checkNotification(userId, notificationId);
    }

    @Operation(summary = "알림 삭제 API")
    @DeleteMapping("/userNotification/{userId}/{notificationId}")
    public ApiResponse deleteNotification(@RequestParam Long userId, @PathVariable Long notificationId) {
        return userNotificationService.deleteUserNotification(userId, notificationId);
    }
}