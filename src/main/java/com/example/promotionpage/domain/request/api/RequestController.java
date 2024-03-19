package com.example.promotionpage.domain.request.api;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.promotionpage.domain.request.application.RequestService;
import com.example.promotionpage.domain.request.dto.request.CreateRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "의뢰 API", description = "의뢰 등록 / 수정 / 삭제 / 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequestController {

	private final RequestService requestService;

	@Operation(summary = "의뢰 등록 API")
	@PostMapping("/requests")
	public ApiResponse createRequest(@Valid @RequestPart("request") CreateRequestDto dto, @RequestPart(value = "files", required = false) List<MultipartFile> files) throws
		IOException {
		return requestService.createRequest(dto.toServiceRequest(), files);
	}

	@Operation(summary = "의뢰 삭제 API")
	@DeleteMapping("/requests/{requestId}")
	public ApiResponse deleteRequest(@PathVariable Long requestId){
		return requestService.deleteRequest(requestId);
	}

	@Operation(summary = "의뢰 전체 조회 API")
	@GetMapping("/requests")
	public ApiResponse retrieveAllRequest(){
		return requestService.retrieveAllRequest();
	}

	@Operation(summary = "의뢰 상세 조회 API")
	@GetMapping("/requests/{requestId}")
	public ApiResponse retrieveRequest(@PathVariable Long requestId){
		return requestService.retrieveRequest(requestId);
	}

}
