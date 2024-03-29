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
    private String id;

    @Column(nullable = false)
    private boolean isRead; // 읽었는지 여부

    @Builder
    public Notification(boolean isRead) {
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
