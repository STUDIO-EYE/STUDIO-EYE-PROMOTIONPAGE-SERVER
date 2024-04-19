package com.example.promotionpage.domain.request.api;

import java.io.IOException;
import java.util.List;

import com.example.promotionpage.domain.request.dto.request.UpdateRequestCommentDto;
import com.example.promotionpage.domain.request.dto.request.UpdateRequestStateDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.promotionpage.domain.request.application.RequestService;
import com.example.promotionpage.domain.request.dto.request.CreateRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "문의 API", description = "문의 등록 / 수정 / 삭제 / 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequestController {

	private final RequestService requestService;

	@Operation(summary = "문의 등록 API")
	@PostMapping("/requests")
	public ApiResponse createRequest(@Valid @RequestPart("request") CreateRequestDto dto, @RequestPart(value = "files", required = false) List<MultipartFile> files) throws
		IOException {
		return requestService.createRequest(dto.toServiceRequest(), files);
	}

	@Operation(summary = "문의 삭제 API")
	@DeleteMapping("/requests/{requestId}")
	public ApiResponse deleteRequest(@PathVariable Long requestId){
		return requestService.deleteRequest(requestId);
	}

	@Operation(summary = "문의 전체 조회 API")
	@GetMapping("/requests")
	public ApiResponse retrieveAllRequest(){
		return requestService.retrieveAllRequest();
	}

	@Operation(summary = "문의 상세 조회 API")
	@GetMapping("/requests/{requestId}")
	public ApiResponse retrieveRequest(@PathVariable Long requestId){
		return requestService.retrieveRequest(requestId);
	}

	@Operation(summary = "전체 문의 수 조회 API")
	@GetMapping("/requests/count")
	public ApiResponse retrieveRequestCount() {
		return requestService.retrieveRequestCount();
	}

	@Operation(summary = "기간(시작점(연도, 월)~종료점(연도, 월))으로 문의 수 조회 API")
	@GetMapping("/requests/{startYear}/{startMonth}/{endYear}/{endMonth}")
	public ApiResponse retrieveRequestCountByPeriod(@PathVariable Integer startYear, @PathVariable Integer startMonth,
											@PathVariable Integer endYear, @PathVariable Integer endMonth) {
		return requestService.retrieveRequestCountByPeriod(startYear, startMonth, endYear, endMonth);
	}

	@Operation(summary = "접수 대기 중인 문의 수 조회 API")
	@GetMapping("/requests/waiting/count")
	public ApiResponse retrieveWaitingRequestCount() {
		return requestService.retrieveWaitingRequestCount();
	}

	@Operation(summary = "접수 대기 중인 문의 목록 조회 API")
	@GetMapping("/requests/waiting")
	public ApiResponse retrieveWaitingRequest() {
		return requestService.retrieveWaitingRequest();
	}

	@Operation(summary = "문의 답변 등록 API")
	@PutMapping("/requests/{requestId}/comment")
	public ApiResponse updateRequestComment(@PathVariable Long requestId, @Valid @RequestBody UpdateRequestCommentDto dto) {
		return requestService.updateRequestComment(requestId, dto.toServiceRequest());
	}

	@Operation(summary = "문의 상태 변경 API")
	@PutMapping("/requests/{requestId}/state")
	public ApiResponse updateRequestComment(@PathVariable Long requestId, @Valid @RequestBody UpdateRequestStateDto dto) {
		return requestService.updateRequestState(requestId, dto.toServiceRequest());
	}

}
