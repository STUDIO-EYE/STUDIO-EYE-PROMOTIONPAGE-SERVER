package com.example.promotionpage.domain.request.application;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.promotionpage.domain.email.service.EmailService;
import com.example.promotionpage.domain.notification.application.NotificationService;

import com.example.promotionpage.domain.request.dao.RequestCount;

import com.example.promotionpage.domain.request.dto.request.*;
import com.example.promotionpage.domain.request.dao.RequestCountImpl;
import com.example.promotionpage.domain.request.dto.request.UpdateRequestCommentServiceDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.promotionpage.domain.request.dao.RequestRepository;
import com.example.promotionpage.domain.request.domain.Request;
import com.example.promotionpage.global.adapter.S3Adapter;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestService {

	private final RequestRepository requestRepository;
	private final S3Adapter s3Adapter;

	private final NotificationService notificationService;
	private final EmailService emailService;

	private static final String EMAIL_REGEX =
			"^[a-zA-Z0-9_+&*-]+(?:\\." +
					"[a-zA-Z0-9_+&*-]+)*@" +
					"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
					"A-Z]{2,7}$";

	private final Integer waitingState = 0;
	private final Integer approvedState = 1;
	private final Integer rejectedState = 2;
	private final Integer completedState = 3;


	private String convertState(Integer state) {
		if(state == this.approvedState) {
			return "승인";
		}
		if(state == this.completedState) {
			return "처리 완료";
		}
		if(state == this.rejectedState) {
			return "거절";
		}
		if(state == this.waitingState) {
			return "대기중";
		}
		return "해당사항 없음";
	}
	public ApiResponse createRequest(CreateRequestServiceDto dto, List<MultipartFile> files) throws IOException {
		if(!isValidEmail(dto.email())) {
			return ApiResponse.withError(ErrorCode.INVALID_EMAIL_FORMAT);
		}
		List<String> fileUrlList = new LinkedList<>();
		if (files != null) {
			for (var file : files) {
				ApiResponse<String> updateFileResponse = s3Adapter.uploadFile(file);

				if (updateFileResponse.getStatus().is5xxServerError()) {
					return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
				}
				String fileUrl = updateFileResponse.getData();
				fileUrlList.add(fileUrl);
			}
		}

		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		Integer year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date().getTime()));
		Integer month = Integer.parseInt(new SimpleDateFormat("MM").format(new Date().getTime()));

		Request request = dto.toEntity(fileUrlList, "", year, month, waitingState, new Date());
		Request savedRequest = requestRepository.save(request);

		String state = convertState(savedRequest.getState());

		String subject = "[studio-eye] 문의가 완료되었습니다."; // 이메일 제목
		String text = "카테고리: " + savedRequest.getCategory() + "\n"
				+ "의뢰인 이름: " + savedRequest.getClientName() + "\n"
				+ "기관 혹은 기업: " + savedRequest.getOrganization() + "\n"
				+ "연락처: " + savedRequest.getContact() + "\n"
				+ "이메일 주소: " + savedRequest.getEmail() + "\n"
				+ "직책: " + savedRequest.getPosition() + "\n"
				+ "의뢰 내용: " + savedRequest.getDescription() + "\n"
				+ "의뢰 상태: " + state;

		emailService.sendEmail(savedRequest.getEmail(), subject, text);
		notificationService.subscribe(request.getId());    // 문의 등록 알림 보내기
		return ApiResponse.ok("문의를 성공적으로 등록하였습니다.", savedRequest);
	}

	public ApiResponse retrieveAllRequest() {
		List<Request> requestList = requestRepository.findAll();

		if (requestList.isEmpty()){
			return ApiResponse.ok("문의가 존재하지 않습니다.");
		}
		return ApiResponse.ok("문의 목록을 성공적으로 조회했습니다.", requestList);
	}

	public ApiResponse retrieveRequest(Long requestId) {
		Optional<Request> optionalRequest = requestRepository.findById(requestId);
		if(optionalRequest.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_REQUEST_ID);
		}

		Request request = optionalRequest.get();
		return ApiResponse.ok("문의를 성공적으로 조회했습니다.", request);
	}

	public ApiResponse retrieveRequestCount() {
		Long requestCount = requestRepository.count();
		return ApiResponse.ok("전체 문의수를 성공적으로 조회했습니다.", requestCount);
	}

	public ApiResponse retrieveRequestCountByPeriod(Integer startYear, Integer startMonth, Integer endYear, Integer endMonth) {
		// 월 형식 검사
		if(!checkMonth(startMonth) || !checkMonth(endMonth)) return ApiResponse.withError(ErrorCode.INVALID_REQUEST_MONTH);
		// 종료점이 시작점보다 앞에 있을 경우 제한 걸기
		if(startYear > endYear || (startYear.equals(endYear) && startMonth>endMonth)) {
			return ApiResponse.withError(ErrorCode.INVALID_PERIOD_FORMAT);
		}
		// 2~12달로 제한 걸기
		Integer months = (endYear - startYear)*12+(endMonth-startMonth);
		if(months < 2 || months > 12) {
			return ApiResponse.withError(ErrorCode.INVALID_REQUEST_PERIOD);
		}
		List<RequestCount> requestCountList = requestRepository.findByYearAndMonthBetween(startYear, startMonth, endYear, endMonth);
		for (int year = startYear; year <= endYear; year++) {
			int monthStart = (year == startYear) ? startMonth : 1;
			int monthEnd = (year == endYear) ? endMonth : 12;

			for (int month = monthStart; month <= monthEnd; month++) {
				boolean found = false;

				// 현재 조회할 연도와 월에 해당하는 인덱스 찾기
				int index = 0;
				for (RequestCount requestCount : requestCountList) {
					// 이미 해당 연도와 월에 대한 데이터가 존재하는 경우
					if (requestCount.getYear() == year && requestCount.getMonth() == month) {
						found = true;
						break;
					}
					// 현재 연도보다 작은 경우 삽입 위치 찾기
					else if (requestCount.getYear() < year || (requestCount.getYear() == year && requestCount.getMonth() < month)) {
						// 삽입 위치 계산
						index++;
					}
				}

				// 해당 연도와 월에 대한 데이터가 존재하지 않는 경우, 0으로 데이터 추가
				if (!found) {
					// 데이터를 삽입한 후에는 인덱스를 증가시킴
					requestCountList.add(index, new RequestCountImpl(year, month, 0L));
				}
			}
		}
		return ApiResponse.ok("문의수 목록을 성공적으로 조회했습니다.", requestCountList);
	}

	public ApiResponse retrieveWaitingRequestCount() {
		Long requestCount = requestRepository.countByState(this.waitingState);
		return ApiResponse.ok("접수 대기 중인 문의 수를 성공적으로 조회했습니다.", requestCount);
	}

	public ApiResponse retrieveWaitingRequest() {
		List<Request> requestList = requestRepository.findByState(this.waitingState);

		if (requestList.isEmpty()){
			return ApiResponse.ok("접수 대기 중인 문의가 존재하지 않습니다.");
		}
		return ApiResponse.ok("접수 대기 중인 문의 목록을 성공적으로 조회했습니다.", requestList);
	}

	public ApiResponse updateRequestState(Long requestId, UpdateRequestStateServiceDto dto) {
		Optional<Request> optionalRequest = requestRepository.findById(requestId);
		if(optionalRequest.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_REQUEST_ID);
		}
		Request request = optionalRequest.get();
		request.updateState(dto.state());
		Request updatedRequest = requestRepository.save(request);

		if(dto.state() != null) {
			String subject = "[studio-eye] [" + convertState(dto.state()) + "]" + updatedRequest.getClientName() + "님의 문의에 의뢰 상태가 " + convertState(dto.state()) + "으로 변경되었습니다."; // 이메일 제목
			String text = "카테고리: " + updatedRequest.getCategory() + "\n"
					+ "의뢰인 이름: " + updatedRequest.getClientName() + "\n"
					+ "기관 혹은 기업: " + updatedRequest.getOrganization() + "\n"
					+ "연락처: " + updatedRequest.getContact() + "\n"
					+ "이메일 주소: " + updatedRequest.getEmail() + "\n"
					+ "직책: " + updatedRequest.getPosition() + "\n"
					+ "의뢰 내용: " + updatedRequest.getDescription() + "\n\n"
					+ "답변: " + updatedRequest.getAnswer() + "\n"
					+ "의뢰 상태: " + convertState(dto.state());

			emailService.sendEmail(updatedRequest.getEmail(), subject, text);
		}

		return ApiResponse.ok("상태를 성공적으로 수정했습니다.");
	}

	public ApiResponse updateRequestComment(Long requestId, UpdateRequestCommentServiceDto dto) {
		String answer = dto.answer().trim();
		Integer state = dto.state();

		Optional<Request> optionalRequest = requestRepository.findById(requestId);
		if(optionalRequest.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_REQUEST_ID);
		}
		Request request = optionalRequest.get();
		request.updateAnswer(answer);
		if(state != null) {
			request.updateState(state);
		}
		Request updatedRequest = requestRepository.save(request);
		
		if(!answer.isEmpty() && state != null) {
			String subject = "[studio-eye] [" + convertState(state) + "]" + updatedRequest.getClientName() + "님의 문의에 답변이 작성되었습니다."; // 이메일 제목
			String text = "카테고리: " + updatedRequest.getCategory() + "\n"
					+ "의뢰인 이름: " + updatedRequest.getClientName() + "\n"
					+ "기관 혹은 기업: " + updatedRequest.getOrganization() + "\n"
					+ "연락처: " + updatedRequest.getContact() + "\n"
					+ "이메일 주소: " + updatedRequest.getEmail() + "\n"
					+ "직책: " + updatedRequest.getPosition() + "\n"
					+ "의뢰 내용: " + updatedRequest.getDescription() + "\n\n"
					+ "답변: " + updatedRequest.getAnswer() + "\n"
					+ "의뢰 상태: " + convertState(state);

			emailService.sendEmail(updatedRequest.getEmail(), subject, text);
		}

		return ApiResponse.ok("답변을 성공적으로 작성했습니다.");
	}

	public ApiResponse deleteRequest(Long requestId) {
		Optional<Request> optionalRequest = requestRepository.findById(requestId);
		if(optionalRequest.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_REQUEST_ID);
		}

		Request request = optionalRequest.get();
		requestRepository.delete(request);

		return ApiResponse.ok("문의를 성공적으로 삭제했습니다.");
	}

	private boolean checkMonth(int month) {
		// 월 형식 검사
		if(month<1 || month>12) return false;
		return true;
	}

	public static boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
