package com.example.promotionpage.domain.request.domain;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.ElementCollection;
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
public class Request {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String category;

	private String clientName;

	private String organization;

	private String contact;

	private String email;

	private String position;

	private String description;

	@ElementCollection
	private List<String> fileUrlList = new LinkedList<>();

	@Builder
	public Request(String category, String clientName, String organization, String contact, String email,
		String position,
		List<String> fileUrlList, String description) {
		this.category = category;
		this.clientName = clientName;
		this.organization = organization;
		this.contact = contact;
		this.email = email;
		this.position = position;
		this.fileUrlList = fileUrlList;
		this.description = description;
	}
}
