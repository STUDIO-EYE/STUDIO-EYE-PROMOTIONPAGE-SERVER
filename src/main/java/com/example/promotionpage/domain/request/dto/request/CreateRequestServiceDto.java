package com.example.promotionpage.domain.request.dto.request;

import java.util.List;

import com.example.promotionpage.domain.request.domain.Request;

public record CreateRequestServiceDto(
	 String category,
	 String clientName,
	 String organization,
	 String contact,
	 String email,
	 String position,
	 String description
) {
	public Request toEntity(List<String> fileUrlList) {
		return Request.builder()
			.category(category)
			.clientName(clientName)
			.organization(organization)
			.contact(contact)
			.email(email)
			.position(position)
			.description(description)
			.fileUrlList(fileUrlList)
			.build();
	}
}
