package com.example.promotionpage.domain.project.api;

import java.util.List;

import com.example.promotionpage.domain.project.dto.request.UpdateProjectTypeDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.promotionpage.domain.project.application.ProjectService;
import com.example.promotionpage.domain.project.dto.request.CreateProjectRequestDto;
import com.example.promotionpage.domain.project.dto.request.UpdatePostingStatusDto;
import com.example.promotionpage.domain.project.dto.request.UpdateProjectRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "프로젝트 API", description = "프로젝트 등록 / 수정 / 삭제 / 조회")
@RestController
// TODO 추후 /api를 /admin으로 변경해야 한다.
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

	// TODO 프로젝트 게시 여부 수정
	private final ProjectService projectService;

	@Operation(summary = "프로젝트 등록 API")
	@PostMapping("/projects")
	public ApiResponse createProject(@Valid @RequestPart("request") CreateProjectRequestDto dto,
									 @RequestPart(value = "file", required = false) MultipartFile mainImgFile,
									 @RequestPart(value = "files", required = false) List<MultipartFile> files){
		return projectService.createProject(dto.toServiceRequest(), mainImgFile, files);
	}

	@Operation(summary = "프로젝트 수정 API")
	@PutMapping("/projects")
	public ApiResponse updateProject(@Valid @RequestPart("request") UpdateProjectRequestDto dto,
									 @RequestPart(value = "file", required = false) MultipartFile mainImgFile,
									 @RequestPart(value = "files", required = false) List<MultipartFile> files){
		return projectService.updateProject(dto.toServiceRequest(), mainImgFile, files);
	}

	@Operation(summary = "프로젝트 삭제 API")
	@DeleteMapping("/projects/{projectId}")
	public ApiResponse deleteProject(@PathVariable Long projectId){
		return projectService.deleteProject(projectId);
	}

	@Operation(summary = "프로젝트 전체 조회 API")
	@GetMapping("/projects")
	public ApiResponse retrieveAllProject(){
		return projectService.retrieveAllProject();
	}

	@Operation(summary = "프로젝트 상세 조회 API")
	@GetMapping("/projects/{projectId}")
	public ApiResponse retrieveProject(@PathVariable Long projectId){
		return projectService.retrieveProject(projectId);
	}

	@Operation(summary = "게시 여부 변경 API")
	@PostMapping("/projects/is-posted")
	public ApiResponse updatePostingStatus(@Valid @RequestBody UpdatePostingStatusDto dto){
		return projectService.updatePostingStatus(dto);
	}

	@Operation(summary = "프로젝트 타입 변경 API")
	@PostMapping("/projects/project-type")
	public ApiResponse updateProjectType(@Valid @RequestBody UpdateProjectTypeDto dto){
		return projectService.updateProjectType(dto);
	}

}
