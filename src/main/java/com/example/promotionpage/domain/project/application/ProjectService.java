package com.example.promotionpage.domain.project.application;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.example.promotionpage.domain.views.application.ViewsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.promotionpage.domain.project.dao.ProjectRepository;
import com.example.promotionpage.domain.project.domain.Project;
import com.example.promotionpage.domain.project.dto.request.CreateProjectServiceRequestDto;
import com.example.promotionpage.domain.project.dto.request.UpdatePostingStatusDto;
import com.example.promotionpage.domain.project.dto.request.UpdateProjectServiceRequestDto;
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

	public ApiResponse createProject(CreateProjectServiceRequestDto dto , MultipartFile mainImgFile, List<MultipartFile> files) {
		String mainImg = getImgUrl(mainImgFile);
		if (mainImg.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);

		List<String> imageUrlList = new LinkedList<>();
		if(files != null){
			for(var file : files){
				String imageUrl = getImgUrl(file);
				if (imageUrl.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
				else imageUrlList.add(imageUrl);
			}
		}

		Project project = dto.toEntity(mainImg, imageUrlList);
		Project savedProject = projectRepository.save(project);
		return ApiResponse.ok("프로젝트를 성공적으로 등록하였습니다.", savedProject);
	}

	public ApiResponse updateProject(UpdateProjectServiceRequestDto dto, MultipartFile mainImgFile, List<MultipartFile> files) {
		Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
		if(optionalProject.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_PROJECT_ID);
		}

		Project project = optionalProject.get();

		String mainImg = getImgUrl(mainImgFile);
		if (mainImg.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);

		List<String> imageUrlList = new LinkedList<>();
		if(files != null){
			for(var file : files){
				String imageUrl = getImgUrl(file);
				if (imageUrl.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
				else imageUrlList.add(imageUrl);
			}
		}

		imageUrlList.addAll(dto.existingImageUrlList());
		Project updatedProject = project.update(dto, mainImg, imageUrlList);

		return ApiResponse.ok("프로젝트를 성공적으로 수정했습니다.", updatedProject);
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
		projectRepository.delete(project);

		return ApiResponse.ok("프로젝트를 성공적으로 삭제했습니다.");
	}

	public ApiResponse retrieveAllProject() {
		// 조회수 증가
		viewsService.updateThisMonthViews();

		List<Project> projectList = projectRepository.findAll();
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
}
