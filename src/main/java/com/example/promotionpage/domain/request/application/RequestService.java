package com.example.promotionpage.domain.request.application;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.promotionpage.domain.notification.application.NotificationService;

import com.example.promotionpage.domain.request.dao.RequestCount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.promotionpage.domain.request.dao.RequestRepository;
import com.example.promotionpage.domain.request.domain.Request;
import com.example.promotionpage.domain.request.dto.request.CreateRequestServiceDto;
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

	public ApiResponse createRequest(CreateRequestServiceDto dto, List<MultipartFile> files) throws IOException {
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

		Request request = dto.toEntity(fileUrlList, year, month);
		Request savedRequest = requestRepository.save(request);

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
		if(startYear > endYear || (startYear == endYear && startMonth>endMonth)) {
			return ApiResponse.withError(ErrorCode.INVALID_PERIOD_FORMAT);
		}
		// 2~12달로 제한 걸기
		Integer months = (endYear - startYear)*12+(endMonth-startMonth);
		if(months < 2 || months > 12) {
			return ApiResponse.withError(ErrorCode.INVALID_REQUEST_PERIOD);
		}
		List<RequestCount> requestCountList = requestRepository.findByYearAndMonthBetween(startYear, startMonth, endYear, endMonth);
		if(requestCountList.isEmpty()) {
			return ApiResponse.ok("문의수가 존재하지 않습니다.");
		}
		return ApiResponse.ok("문의수 목록을 성공적으로 조회했습니다.", requestCountList);
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
}
