package com.example.promotionpage.domain.request.domain;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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

	private String answer;

	@Column(name = "year_value")
	private Integer year;

	@Column(name = "month_value")
	private Integer month;

	private Integer state;

	@ElementCollection
	private List<String> fileUrlList = new LinkedList<>();

	@Builder
	public Request(String category, String clientName, String organization, String contact, String email,
				   String position,
				   List<String> fileUrlList, String description, String answer,
				   Integer year, Integer month, Integer state) {
		this.category = category;
		this.clientName = clientName;
		this.organization = organization;
		this.contact = contact;
		this.email = email;
		this.position = position;
		this.fileUrlList = fileUrlList;
		this.description = description;
		this.answer = answer;
		this.year = year;
		this.month = month;
		this.state = state;
	}

	public void updateAnswer(String answer) {
		this.answer = answer;
	}
	public void updateState(Integer state) {
		this.state = state;
	}
}
