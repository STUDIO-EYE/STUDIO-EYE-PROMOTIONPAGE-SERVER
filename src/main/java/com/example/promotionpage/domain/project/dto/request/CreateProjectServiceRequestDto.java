package com.example.promotionpage.domain.project.dto.request;

import java.util.List;

import com.example.promotionpage.domain.project.domain.Project;
import com.example.promotionpage.domain.project.domain.ProjectImage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateProjectServiceRequestDto(
	String department,
	String category,
	String name,
	String client,
	String date,
	String link,
	String overView,
	String projectType,
	Boolean isPosted
) {
	public Project toEntity(String mainImg, List<ProjectImage> projectImages, long projectCount, Integer mainSequence) {
		return Project.builder()
			.department(department)
			.category(category)
			.name(name)
			.client(client)
			.date(date)
			.link(link)
			.overView(overView)
			.mainImg(mainImg)
			.projectImages(projectImages)
			.sequence((int) (projectCount + 1))
			.mainSequence(mainSequence)
			.projectType(projectType)
			.isPosted(isPosted)
			.build();
	}
}
