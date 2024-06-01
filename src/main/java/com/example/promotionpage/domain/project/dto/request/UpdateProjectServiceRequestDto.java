package com.example.promotionpage.domain.project.dto.request;

import java.util.List;

public record UpdateProjectServiceRequestDto(
	Long projectId,
	String department,
	String category,
	String name,
	String client,
	String date,
	String link,
	String overView,
	List<Long> deletedImagesId,
	String projectType,
	Boolean isPosted
) {
}
