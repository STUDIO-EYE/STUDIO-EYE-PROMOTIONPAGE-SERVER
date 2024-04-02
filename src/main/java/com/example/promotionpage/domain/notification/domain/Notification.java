package com.example.promotionpage.domain.notification.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.lang.reflect.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isRead; // 읽었는지 여부

    @Column(nullable = false)
    private Long requestId; // 문의 ID


    @Builder
    public Notification(Boolean isRead, Long requestId) {
        this.isRead = isRead;
        this.requestId = requestId;
    }

    public void updateIsChecked(Boolean isRead) {
        this.isRead = isRead;
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private String id;
//
////    @Embedded
////    private NotificationContent content;
//
////    @Embedded
////    private RelatedURL url;
//
////    @Column(nullable = false)
//    private boolean isRead; // 읽었는지 여부
//
////    @Enumerated(EnumType.STRING)
////    @Column(nullable = false)
////    private NotificationType notificationType; // 알림 종류
//
//    @Builder
//    public Notification(boolean isRead) {
////        this.notificationType = notificationType;
////        this.content = new NotificationContent(content);
////        this.url = new RelatedURL(url);
//        this.isRead = isRead;
//    }
//
////    public String getContent() {
////        return content.getContent();
////    }
//
////    public String getUrl() {
////        return url.getUrl();
////    }
}