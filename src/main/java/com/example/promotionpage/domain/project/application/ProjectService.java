package com.example.promotionpage.domain.project.application;

import java.util.ArrayList;
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
		// 삭제할 이미지 ID 목록이 비어있지 않은 경우 삭제 처리
		if (dto.deletedImagesId() != null && !dto.deletedImagesId().isEmpty()) {
			List<Long> deletedImagesIdList = dto.deletedImagesId();
			List<ProjectImage> imagesToRemove = project.getProjectImages().stream()
					.filter(image -> deletedImagesIdList.contains(image.getId()))
					.toList();

			for (ProjectImage image : imagesToRemove) {
				String fileName = image.getFileName();
				// S3에서 해당 이미지 삭제
				s3Adapter.deleteFile(fileName);
				project.getProjectImages().remove(image);
			}
		}

		// 새로운 메인 이미지가 전달된 경우에만 새로 저장
		if (mainImgFile != null && !mainImgFile.isEmpty()) {
			String mainImg = getImgUrl(mainImgFile);
			if (mainImg.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
			project.setMainImg(mainImg);
		}

		// 새로운 이미지들 추가
		if (files != null && !files.isEmpty()) {
			List<ProjectImage> projectImages = new LinkedList<>();
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

	// 프로젝트 순서 변경 : artwork page
	public ApiResponse changeSequenceProject(List<ChangeSequenceProjectReq> changeSequenceProjectReqList) {

		for (ChangeSequenceProjectReq changeSequenceProjectReq : changeSequenceProjectReqList) {
			Optional<Project> findProject = projectRepository.findById(changeSequenceProjectReq.getProjectId());
			if (findProject.isEmpty())
				return ApiResponse.withError(ErrorCode.INVALID_PROJECT_ID);
			Project project = findProject.get();
			project.updateSequence(changeSequenceProjectReq.getSequence());
		}

		return ApiResponse.ok("아트워크 페이지에 보여질 프로젝트의 순서를 성공적으로 수정하였습니다.");
	}

	// 프로젝트 순서 변경 : main page
	public ApiResponse changeMainSequenceProject(List<ChangeMainSequenceProjectReq> changeMainSequenceProjectReqList) {

		for (ChangeMainSequenceProjectReq changeMainSequenceProjectReq : changeMainSequenceProjectReqList) {
			Optional<Project> findProject = projectRepository.findById(changeMainSequenceProjectReq.getProjectId());
			if (findProject.isEmpty())
				return ApiResponse.withError(ErrorCode.INVALID_PROJECT_ID);
			Project project = findProject.get();
			if (!project.getProjectType().equals(MAIN_PROJECT_TYPE))
				return ApiResponse.withError(ErrorCode.INVALID_PROJECT_ID);
			project.updateMainSequence(changeMainSequenceProjectReq.getMainSequence());
		}

		return ApiResponse.ok("메인 페이지에 보여질 프로젝트의 순서를 성공적으로 수정하였습니다.");
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
		Integer mainSequence = project.getMainSequence();
		projectRepository.delete(project);

		List<Project> findBySequenceGreaterThan = projectRepository.findAllBySequenceGreaterThan(sequence);
		List<Project> findByMainSequenceGreaterThan = projectRepository.findAllByMainSequenceGreaterThanAndMainSequenceNot(mainSequence, 999);
		for (Project findArtworkProject : findBySequenceGreaterThan) {
			findArtworkProject.updateSequence(findArtworkProject.getSequence() - 1);
		}

		if (project.getProjectType().equals(MAIN_PROJECT_TYPE)) {
			for (Project findMainProject : findByMainSequenceGreaterThan) {
				findMainProject.updateMainSequence(findMainProject.getMainSequence() - 1);
			}
		}
		return ApiResponse.ok("프로젝트를 성공적으로 삭제했습니다.");
	}

	// for artwork page
	public ApiResponse retrieveAllArtworkProject() {
		// 조회수 증가
//		viewsService.updateThisMonthViews();
		List<Project> projectList = projectRepository.findAllWithImagesAndOrderBySequenceAsc();
		if (projectList.isEmpty()){
			return ApiResponse.ok("프로젝트가 존재하지 않습니다.");
		}

		return ApiResponse.ok("프로젝트 목록을 성공적으로 조회했습니다.", projectList);
	}

	// for main page
	public ApiResponse retrieveAllMainProject() {
		// 조회수 증가
		List<Project> projectList = projectRepository.findAllWithImagesAndOrderByMainSequenceAsc();
		List<Project> responseProject = new ArrayList<>();
		List<Project> topProject = projectRepository.findByProjectType(TOP_PROJECT_TYPE);
		Project top;
		if (!topProject.isEmpty()) {
			top = topProject.get(0);
			responseProject.add(top);
		}
		for (Project project : projectList) {
			responseProject.add(project);
		}

		if (projectList.isEmpty()){
			return ApiResponse.ok("프로젝트가 존재하지 않습니다.");
		}

		return ApiResponse.ok("프로젝트 목록을 성공적으로 조회했습니다.", responseProject);

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
//				if (project.getProjectType().equals(MAIN_PROJECT_TYPE))
//					project.updateMainSequence(999);
			case MAIN_PROJECT_TYPE:
				List<Project> mainProjects = projectRepository.findByProjectType(MAIN_PROJECT_TYPE);
				// "main"인 프로젝트가 이미 5개 이상인 경우
				if (mainProjects.size() >= 5) {
					return ApiResponse.withError(ErrorCode.MAIN_PROJECT_LIMIT_EXCEEDED);
				}
				Project updatedMainProject = project.updateProjectType(projectType);
				Integer mainSequence = projectRepository.countByProjectType(projectType);
				project.updateMainSequence(mainSequence + 1);
				return ApiResponse.ok("프로젝트 타입을 성공적으로 변경하였습니다.", updatedMainProject);
			case OTHERS_PROJECT_TYPE:
				if (project.getProjectType().equals(MAIN_PROJECT_TYPE))
					project.updateMainSequence(999);

				Project updatedProject = project.updateProjectType(dto.projectType());
				return ApiResponse.ok("프로젝트 타입을 성공적으로 변경하였습니다.", updatedProject);
			default: // 유효하지 않은 값일 경우
				return ApiResponse.withError(ErrorCode.INVALID_PROJECT_TYPE);
		}
	}

}
