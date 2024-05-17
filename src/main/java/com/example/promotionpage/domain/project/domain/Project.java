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
	private static final String OTHERS_PROJECT_TYPE = "others";

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

	private String projectType;

	private Boolean isPosted;

	private String mainImg;

	private Integer sequence;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<ProjectImage> projectImages = new LinkedList<>();

	@Builder
	public Project(String department, String category, String name, String client, String date, String link,
		String overView, String mainImg, List<ProjectImage> projectImages, Integer sequence) {
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
		this.projectType = OTHERS_PROJECT_TYPE;
		this.sequence = sequence;
	}

	public Project update(UpdateProjectServiceRequestDto dto) {
		this.department = dto.department();
		this.category = dto.category();
		this.name = dto.name();
		this.client = dto.client();
		this.date = dto.date();
		this.link = dto.link();
		this.overView = dto.overView();
		return this;
	}

	public void updateSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Project updatePostingStatus(Boolean isPosted) {
		this.isPosted = isPosted;
		return this;
	}

	public Project updateProjectType(String projectType) {
		this.projectType = projectType;
		return this;
	}
}
