package com.example.promotionpage.domain.noticeBoard.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.promotionpage.domain.noticeBoard.application.NoticeBoardService;
import com.example.promotionpage.domain.noticeBoard.dto.request.CreateNoticeBoardRequestDto;
import com.example.promotionpage.domain.noticeBoard.dto.request.UpdateNoticeBoardRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Tag(name = "공지 사항 API", description = "공지 사항 등록 / 수정 / 삭제 / 조회")
@RestController
// TODO 추후 /api를 /admin으로 변경해야 한다.
@RequestMapping("/api")
@RequiredArgsConstructor
public class NoticeBoardController {

	private final NoticeBoardService noticeBoardService;


	@Operation(summary = "공지 사항 등록 API")
	@PostMapping("/notice-board")
	public ApiResponse createNoticeBoard(@RequestPart(required = true) MultipartFile file, @Valid @RequestPart(value = "createNoticeBoardRequestDto") CreateNoticeBoardRequestDto dto){
		return noticeBoardService.createNoticeBoard(file, dto.title());
	}
	
	@Operation(summary = "공지 사항 수정 API")
	@PutMapping("/notice-board")
	public ApiResponse updateNoticeBoard(@RequestPart(required = false) MultipartFile file, @Valid @RequestPart(value = "updateNoticeBoardRequestDto") UpdateNoticeBoardRequestDto dto){
		return noticeBoardService.updateNoticeBoard(file, dto.title(), dto.noticeBoardId());
	}

	@Operation(summary = "공지 사항 삭제 API")
	@DeleteMapping("/notice-board")
	public ApiResponse deleteNoticeBoard(@RequestBody Long noticeBoardId){
		return noticeBoardService.deleteNoticeBoard(noticeBoardId);
	}

	@Operation(summary = "공지 사항 전체 조회 API")
	@GetMapping("/notice-board")
	public ApiResponse retrieveAllNoticeBoard(){
		return noticeBoardService.retrieveAllNoticeBoard();
	}

	@Operation(summary = "공지 사항 상세 조회 API")
	@GetMapping("/notice-board/{noticeBoardId}")
	public ApiResponse retrieveNoticeBoard(@PathVariable Long noticeBoardId){
		return noticeBoardService.retrieveNoticeBoard(noticeBoardId);
	}


}
