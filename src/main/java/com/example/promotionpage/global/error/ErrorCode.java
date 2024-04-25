package com.example.promotionpage.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// Common

	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Request Body를 통해 전달된 값이 유효하지 않습니다."),

	// S3
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 문제로 S3 이미지 업로드에 실패하였습니다."),
	NOT_EXIST_IMAGE_FILE(HttpStatus.BAD_REQUEST, "업로드 할 이미지가 존재하지 않습니다."),

	ERROR_S3_DELETE_OBJECT(HttpStatus.INTERNAL_SERVER_ERROR, "서버 문제 S3 이미지 삭제에 실패하였습니다."),
	ERROR_S3_UPDATE_OBJECT(HttpStatus.INTERNAL_SERVER_ERROR, "서버 문제로 S3 이미지 업로드에 실패하였습니다."),


	// notice board
	INVALID_NOTICE_BOARD_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 notice board 식별자입니다."),

	// project
	INVALID_PROJECT_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 project 식별자입니다."),
	INVALID_PROJECT_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 project type입니다."),
	TOP_PROJECT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "TOP PROJECT가 이미 존재합니다."),
	MAIN_PROJECT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "MAIN PROJECT의 개수 제한을 초과했습니다."),

	// partner information
	INVALID_PARTNER_INFORMATION_ID(HttpStatus.BAD_REQUEST,"유효하지 않은 partner information 식별자입니다."),

	// request
	INVALID_REQUEST_ID(HttpStatus.BAD_REQUEST,"유효하지 않은 request 식별자입니다."),
	INVALID_REQUEST_MONTH(HttpStatus.BAD_REQUEST, "유효하지 않은 월 형식입니다."),
	INVALID_REQUEST_PERIOD(HttpStatus.BAD_REQUEST, "기간은 2~12달이어야 합니다."),
	INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다."),

	// views
	INVALID_VIEWS_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 views 식별자입니다."),
	INVALID_VIEWS_MONTH(HttpStatus.BAD_REQUEST, "유효하지 않은 월 형식입니다."),
	ALREADY_EXISTED_DATA(HttpStatus.BAD_REQUEST, "이미 존재하는 데이터입니다."),
	INVALID_VIEWS_PERIOD(HttpStatus.BAD_REQUEST, "기간은 2~12달이어야 합니다."),
	INVALID_PERIOD_FORMAT(HttpStatus.BAD_REQUEST, "종료점은 시작점보다 뒤에 있어야 합니다."),

	// notification
	USER_IS_EMPTY(HttpStatus.BAD_REQUEST, "User가 존재하지 않습니다."),
	INVALID_NOTIFICATION_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 notification 식별자입니다."),

	// user notification
	INVALID_USER_NOTIFICATION_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 user 혹은 notification 식별자입니다."),
	FAILED_USER_NOTIFICATION_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "알림 삭제에 실패하였습니다."),

	// sseEmitter
	INVALID_SSE_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 sse 식별자입니다."),

	// FAQ
	INVALID_FAQ_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 views 식별자입니다."),
	FAQ_IS_EMPTY(HttpStatus.BAD_REQUEST, "입력된 내용이 없습니다");

	private final HttpStatus status;
	private final String message;

}