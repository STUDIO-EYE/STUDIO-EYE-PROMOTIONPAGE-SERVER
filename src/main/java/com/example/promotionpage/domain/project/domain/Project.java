package com.example.promotionpage.domain.project.domain;

import java.util.LinkedList;
import java.util.List;

import com.example.promotionpage.domain.project.dto.request.UpdateProjectServiceRequestDto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String department;

	private String category;

	private String name;

	private String client;

	private String date;

	private String link;

	private String overView;

	private Boolean isPosted;

	private String mainImg;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<ProjectImage> projectImages = new LinkedList<>();

	@Builder
	public Project(String department, String category, String name, String client, String date, String link,
		String overView, String mainImg, List<ProjectImage> projectImages) {
		this.department = department;
		this.category = category;
		this.name = name;
		this.client = client;
		this.date = date;
		this.link = link;
		this.overView = overView;
		this.mainImg = mainImg;
		this.projectImages = projectImages;
		this.isPosted = false;
	}

	public Project updatePostingStatus(Boolean isPosted) {
		this.isPosted = isPosted;
		return this;
	}
}
