package com.example.promotionpage.domain.views.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Views {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "year_value")
	private Integer year;

	@Column(name = "month_value")
	private Integer month;

	private Long views;

	@Builder
	public Views(Integer year, Integer month, Long views) {
		this.year = year;
		this.month = month;
		this.views = views;
	}

	public void updateViews(Long views) {
		this.views = views;
	}
}
