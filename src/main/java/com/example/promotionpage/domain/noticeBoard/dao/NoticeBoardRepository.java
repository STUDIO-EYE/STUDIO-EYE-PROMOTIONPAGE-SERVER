package com.example.promotionpage.domain.noticeBoard.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.promotionpage.domain.noticeBoard.domain.NoticeBoard;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {
}
