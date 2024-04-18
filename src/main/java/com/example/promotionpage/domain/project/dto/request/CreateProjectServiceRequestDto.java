package com.example.promotionpage.domain.project.dto.request;

import java.util.List;

import com.example.promotionpage.domain.project.domain.Project;

public record CreateProjectServiceRequestDto(
	String department,
	String category,
	String name,
	String client,
	String date,
	String link,
	String overView
) {
	public Project toEntity(String mainImg, List<String> imageUrlList) {
		return Project.builder()
			.department(department)
			.category(category)
			.name(name)
			.client(client)
			.date(date)
			.link(link)
			.overView(overView)
			.mainImg(mainImg)
			.imageUrlList(imageUrlList)
			.build();
	}
}
