package com.example.promotionpage.domain.noticeBoard.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeBoard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String imageUrl;

	@Builder
	public NoticeBoard(String title, String imageUrl) {
		this.title = title;
		this.imageUrl = imageUrl;
	}

	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateImageUrl(String updatedImageUrl) {
		this.imageUrl = updatedImageUrl;
	}
}
