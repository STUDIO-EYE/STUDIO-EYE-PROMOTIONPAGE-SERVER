package com.example.promotionpage.domain.project.application;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.example.promotionpage.domain.project.domain.ProjectImage;
import com.example.promotionpage.domain.project.dto.request.*;
import com.example.promotionpage.domain.views.application.ViewsService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.promotionpage.domain.project.dao.ProjectRepository;
import com.example.promotionpage.domain.project.domain.Project;
import com.example.promotionpage.global.adapter.S3Adapter;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final S3Adapter s3Adapter;
	private final ViewsService viewsService;
	private static final String TOP_PROJECT_TYPE = "top";
	private static final String MAIN_PROJECT_TYPE = "main";
	private static final String OTHERS_PROJECT_TYPE = "others";

	public ApiResponse createProject(CreateProjectServiceRequestDto dto, MultipartFile mainImgFile, List<MultipartFile> files) {
		String mainImg = getImgUrl(mainImgFile);
		if (mainImg.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);

		List<ProjectImage> projectImages = new LinkedList<>();
		if (files != null) {
			for (MultipartFile file : files) {
				String imageUrl = getImgUrl(file);
				if (imageUrl.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);

				String fileName = file.getOriginalFilename(); // 원본 파일 이름 가져오기
				ProjectImage projectImage = ProjectImage.builder()
						.imageUrlList(imageUrl)
						.fileName(fileName)
						.build();
				projectImages.add(projectImage);
			}
		}
		long count = projectRepository.count();
		Project project = dto.toEntity(mainImg, projectImages, count);

		// ProjectImage의 project 필드 설정
		for (ProjectImage projectImage : projectImages) {
			projectImage.setProject(project);
		}

		Project savedProject = projectRepository.save(project);
		return ApiResponse.ok("프로젝트를 성공적으로 등록하였습니다.", savedProject);
	}

	public ApiResponse updateProject(UpdateProjectServiceRequestDto dto, MultipartFile mainImgFile, List<MultipartFile> files) {
		Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
		if(optionalProject.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_PROJECT_ID);
		}

		Project project = optionalProject.get();
		// 기존 이미지들 전체 삭제
		List<ProjectImage> existingImages = project.getProjectImages();
		// S3에서 기존 이미지들 삭제
		for (ProjectImage image : existingImages) {
			String fileName = image.getFileName();
			// S3Adapter의 deleteFile 메소드를 호출하여 이미지를 삭제
			s3Adapter.deleteFile(fileName);
		}
		project.getProjectImages().clear();

		// 새로운 메인이미지 저장
		String mainImg = getImgUrl(mainImgFile);
		if (mainImg.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
		project.setMainImg(mainImg);

		// 기존 이미지 + 새로운 이미지들 저장
		List<ProjectImage> projectImages = new LinkedList<>();
		if (files != null) {
			for (MultipartFile file : files) {
				String imageUrl = getImgUrl(file);
				if (imageUrl.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);

				String fileName = file.getOriginalFilename(); // 원본 파일 이름 가져오기
				ProjectImage projectImage = ProjectImage.builder()
						.project(project)
						.imageUrlList(imageUrl)
						.fileName(fileName)
						.build();
				projectImages.add(projectImage);
			}
			project.getProjectImages().addAll(projectImages);
		}

		Project updatedProject = projectRepository.save(project);
		updatedProject.update(dto);
		return ApiResponse.ok("프로젝트를 성공적으로 수정했습니다.", updatedProject);
	}

	// 프로젝트 순서 변경
	public ApiResponse changeSequenceProject(List<ChangeSequenceProjectReq> changeSequenceProjectReqList) {

		for (ChangeSequenceProjectReq changeSequenceProjectReq : changeSequenceProjectReqList) {
			Optional<Project> findProject = projectRepository.findById(changeSequenceProjectReq.getProjectId());
			if (findProject.isEmpty())
				return ApiResponse.withError(ErrorCode.INVALID_PROJECT_ID);
			Project project = findProject.get();
			project.updateSequence(changeSequenceProjectReq.getSequence());
		}

		return ApiResponse.ok("프로젝트의 순서를 성공적으로 수정하였습니다.");

	}

	private String getImgUrl(MultipartFile file) {
		ApiResponse<String> updateFileResponse = s3Adapter.uploadImage(file);

		if(updateFileResponse.getStatus().is5xxServerError()){

			return "";
		}
		String imageUrl = updateFileResponse.getData();
		return imageUrl;
	}

	public ApiResponse deleteProject(Long projectId) {
		Optional<Project> optionalProject = projectRepository.findById(projectId);
		if(optionalProject.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_PROJECT_ID);
		}

		Project project = optionalProject.get();
		Integer sequence = project.getSequence();
		projectRepository.delete(project);

		List<Project> findBySequenceGreaterThan = projectRepository.findAllBySequenceGreaterThan(sequence);
		for (Project findProject : findBySequenceGreaterThan) {
			findProject.updateSequence(findProject.getSequence() - 1);
		}

		return ApiResponse.ok("프로젝트를 성공적으로 삭제했습니다.");
	}

	public ApiResponse retrieveAllProject() {
		// 조회수 증가
		viewsService.updateThisMonthViews();
		List<Project> projectList = projectRepository.findAllWithImagesAndOrderBySequenceAsc();
		if (projectList.isEmpty()){
			return ApiResponse.ok("프로젝트가 존재하지 않습니다.");
		}

		return ApiResponse.ok("프로젝트 목록을 성공적으로 조회했습니다.", projectList);
	}

	public ApiResponse retrieveProject(Long projectId) {
		Optional<Project> optionalProject = projectRepository.findById(projectId);
		if(optionalProject.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_PROJECT_ID);
		}

		Project project = optionalProject.get();
		return ApiResponse.ok("프로젝트를 성공적으로 조회했습니다.", project);
	}

	public ApiResponse updatePostingStatus(UpdatePostingStatusDto dto) {
		Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
		if(optionalProject.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_PROJECT_ID);
		}

		Project project = optionalProject.get();
		Project updatedProject = project.updatePostingStatus(dto.isPosted());

		return ApiResponse.ok("프로젝트 게시 여부를 성공적으로 변경하였습니다.", updatedProject);

	}

	public ApiResponse updateProjectType(UpdateProjectTypeDto dto) {
		String projectType = dto.projectType();
		Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
		if(optionalProject.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_PROJECT_ID);
		}
		Project project = optionalProject.get();

		switch (projectType) {
			// 받아온 projectType String 값이 유효한 경우
			case TOP_PROJECT_TYPE:
				List<Project> topProject = projectRepository.findByProjectType(projectType);
				// TOP 프로젝트가 이미 존재하고, 전달된 프로젝트 id가 이미 존재하는 TOP 프로젝트 id와 다른 경우
				if (!topProject.isEmpty() && !project.getId().equals(topProject.get(0).getId())) {
					return ApiResponse.withError(ErrorCode.TOP_PROJECT_ALREADY_EXISTS);
				}
			case MAIN_PROJECT_TYPE:
				List<Project> mainProjects = projectRepository.findByProjectType(MAIN_PROJECT_TYPE);
				// "main"인 프로젝트가 이미 5개 이상인 경우
				if (mainProjects.size() >= 5) {
					return ApiResponse.withError(ErrorCode.MAIN_PROJECT_LIMIT_EXCEEDED);
				}
			case OTHERS_PROJECT_TYPE:
				Project updatedProject = project.updateProjectType(dto.projectType());
				return ApiResponse.ok("프로젝트 타입을 성공적으로 변경하였습니다.", updatedProject);
			default: // 유효하지 않은 값일 경우
				return ApiResponse.withError(ErrorCode.INVALID_PROJECT_TYPE);
		}
	}


}
