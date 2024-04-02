package com.example.promotionpage.domain.request.application;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.example.promotionpage.domain.notification.application.NotificationService;
import com.example.promotionpage.domain.notification.dto.request.CreateNotificationServiceRequestDto;
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
		if(files != null){
			for(var file : files){
				ApiResponse<String> updateFileResponse = s3Adapter.uploadFile(file);

				if(updateFileResponse.getStatus().is5xxServerError()){
					return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
				}
				String fileUrl = updateFileResponse.getData();
				fileUrlList.add(fileUrl);
			}
		}

		Request request = dto.toEntity(fileUrlList);
		Request savedRequest = requestRepository.save(request);

		notificationService.justCreateNotification(request.getId());	// 문의 등록 알림 보내기
		return ApiResponse.ok("프로젝트를 성공적으로 등록하였습니다.", savedRequest);
	}

	public ApiResponse retrieveAllRequest() {
		List<Request> requestList = requestRepository.findAll();
		if (requestList.isEmpty()){
			return ApiResponse.ok("의뢰가 존재하지 않습니다.");
		}

		return ApiResponse.ok("의뢰 목록을 성공적으로 조회했습니다.", requestList);
	}

	public ApiResponse retrieveRequest(Long requestId) {
		Optional<Request> optionalRequest = requestRepository.findById(requestId);
		if(optionalRequest.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_REQUEST_ID);
		}

		Request request = optionalRequest.get();
		return ApiResponse.ok("프로젝트를 성공적으로 조회했습니다.", request);
	}

	public ApiResponse deleteRequest(Long requestId) {
		Optional<Request> optionalRequest = requestRepository.findById(requestId);
		if(optionalRequest.isEmpty()){
			return ApiResponse.withError(ErrorCode.INVALID_REQUEST_ID);
		}

		Request request = optionalRequest.get();
		requestRepository.delete(request);

		return ApiResponse.ok("의뢰를 성공적으로 삭제했습니다.");
	}
}
