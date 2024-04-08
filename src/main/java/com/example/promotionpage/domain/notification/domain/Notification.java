package com.example.promotionpage.domain.notification.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void updateIsRead(Boolean isRead) {
        this.isRead = isRead;
   }
}