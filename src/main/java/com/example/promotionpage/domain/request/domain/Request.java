package com.example.promotionpage.domain.request.domain;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.*;
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

	@Column(name = "year_value")
	private Integer year;

	@Column(name = "month_value")
	private Integer month;

	@ElementCollection
	private List<String> fileUrlList = new LinkedList<>();

	@Builder
	public Request(String category, String clientName, String organization, String contact, String email,
				   String position,
				   List<String> fileUrlList, String description,
				   Integer year, Integer month) {
		this.category = category;
		this.clientName = clientName;
		this.organization = organization;
		this.contact = contact;
		this.email = email;
		this.position = position;
		this.fileUrlList = fileUrlList;
		this.description = description;
		this.year = year;
		this.month = month;
	}
}
