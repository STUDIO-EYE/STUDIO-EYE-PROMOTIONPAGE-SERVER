package com.example.promotionpage.domain.noticeBoard.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.promotionpage.domain.noticeBoard.dao.NoticeBoardRepository;
import com.example.promotionpage.domain.noticeBoard.domain.NoticeBoard;
import com.example.promotionpage.global.adapter.S3Adapter;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeBoardService {

	private final NoticeBoardRepository noticeBoardRepository;
	private final S3Adapter s3Adapter;

	public ApiResponse createNoticeBoard(MultipartFile file, String title) {
		ApiResponse<String> updateFileResponse = s3Adapter.uploadImage(file);
		if(updateFileResponse.getStatus().is5xxServerError()){
			return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
		}
		String imageUrl = updateFileResponse.getData();

		NoticeBoard noticeBoard = NoticeBoard.builder()
			.title(title)
			.imageUrl(imageUrl)
			.build();

		NoticeBoard savedNoticeBoard = noticeBoardRepository.save(noticeBoard);
		return ApiResponse.ok("공지 사항을 성공적으로 등록하였습니다.", savedNoticeBoard);
	}

	private ApiResponse<String> uploadImage(MultipartFile multipartFile) {
		if (multipartFile == null) {
			return ApiResponse.withError(ErrorCode.NOT_EXIST_IMAGE_FILE);
		}
		return s3Adapter.uploadImage(multipartFile);
	}

	public ApiResponse updateNoticeBoard(MultipartFile file, String title, Long noticeBoardId) {
		Optional<NoticeBoard> optionalNoticeBoard = noticeBoardRepository.findById(noticeBoardId);
		if(optionalNoticeBoard.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_NOTICE_BOARD_ID);
		}

		NoticeBoard noticeBoard = optionalNoticeBoard.get();
		noticeBoard.updateTitle(title);

		if(file.isEmpty()){
			NoticeBoard updatedNoticeBoard = noticeBoardRepository.save(noticeBoard);
			return ApiResponse.ok("공지 사항을 제목을 성공적으로 수정하였습니다.", updatedNoticeBoard);
		}

		ApiResponse<String> deleteFileResponse = s3Adapter.deleteFile(noticeBoard.getImageUrl().split("/")[3]);
		if(deleteFileResponse.getStatus().is5xxServerError()){
			return ApiResponse.withError(ErrorCode.ERROR_S3_DELETE_OBJECT);
		}

		ApiResponse<String> updateFileResponse = s3Adapter.uploadImage(file);
		if(updateFileResponse.getStatus().is5xxServerError()){
			return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
		}

		String updatedImageUrl = updateFileResponse.getData();
		noticeBoard.updateImageUrl(updatedImageUrl);

		NoticeBoard updatedNoticeBoard = noticeBoardRepository.save(noticeBoard);
		return ApiResponse.ok("공지 사항을 성공적으로 수정하였습니다.", updatedNoticeBoard);
	}

	public ApiResponse deleteNoticeBoard(Long noticeBoardId) {
		Optional<NoticeBoard> optionalNoticeBoard = noticeBoardRepository.findById(noticeBoardId);
		if(optionalNoticeBoard.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_NOTICE_BOARD_ID);
		}

		NoticeBoard noticeBoard = optionalNoticeBoard.get();
		ApiResponse<String> deleteFileResponse = s3Adapter.deleteFile(noticeBoard.getImageUrl().split("/")[3]);
		if(deleteFileResponse.getStatus().is5xxServerError()){
			return ApiResponse.withError(ErrorCode.ERROR_S3_DELETE_OBJECT);
		}

		noticeBoardRepository.delete(noticeBoard);
		return ApiResponse.ok("공지 사항을 성공적으로 삭제하였습니다.");
	}

	public ApiResponse retrieveAllNoticeBoard() {
		List<NoticeBoard> noticeBoardList = noticeBoardRepository.findAll();
		if(noticeBoardList.isEmpty()){
			return ApiResponse.ok("공지 사항이 존재하지 않습니다.");
		}
		return ApiResponse.ok("공지 사항 전체 목록을 성공적으로 조회하였습니다.", noticeBoardList);
	}

	public ApiResponse retrieveNoticeBoard(Long noticeBoardId) {
		Optional<NoticeBoard> optionalNoticeBoard = noticeBoardRepository.findById(noticeBoardId);
		if(optionalNoticeBoard.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_NOTICE_BOARD_ID);
		}
		return ApiResponse.ok("공지 사항을 성공적으로 조회하였습니다.", optionalNoticeBoard.get());
	}
}
