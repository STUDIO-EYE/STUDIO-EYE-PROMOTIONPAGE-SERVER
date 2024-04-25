package com.example.promotionpage.domain.client.application;

import com.example.promotionpage.domain.client.dao.ClientRepository;
import com.example.promotionpage.domain.client.domain.Client;
import com.example.promotionpage.domain.client.dto.request.CreateClientServiceRequestDto;
import com.example.promotionpage.domain.client.dto.request.UpdateClientServiceRequestDto;
import com.example.promotionpage.global.adapter.S3Adapter;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final S3Adapter s3Adapter;

    public ApiResponse createClient(CreateClientServiceRequestDto dto, MultipartFile logoImg) {
        String logoImgStr = getImgUrl(logoImg);
        if (logoImgStr.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);

        Client client = dto.toEntity(logoImgStr);

        Client savedClient = clientRepository.save(client);
        return ApiResponse.ok("클라이언트를 성공적으로 등록하였습니다.", savedClient);
    }

    public ApiResponse updateClient(UpdateClientServiceRequestDto dto, MultipartFile logoImg) {
        Optional<Client> optionalClient = clientRepository.findById(dto.clientId());
        if(optionalClient.isEmpty()){
            return ApiResponse.withError(ErrorCode.INVALID_CLIENT_ID);
        }

        Client client = optionalClient.get();

        // 새로운 로고이미지 저장
        String logoImgStr = getImgUrl(logoImg);
        if (logoImgStr.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
        client.setLogoImg(logoImgStr);

        Client updatedClient = clientRepository.save(client);
        updatedClient.update(dto);
        return ApiResponse.ok("클라이언트를 성공적으로 수정했습니다.", updatedClient);
    }

    public ApiResponse updateClientLogoImg(Long clientId, MultipartFile logoImg) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isEmpty()) {
            return ApiResponse.withError(ErrorCode.INVALID_CLIENT_ID);
        }

        Client client = optionalClient.get();

        // 새로운 로고이미지 저장
        String logoImgStr = getImgUrl(logoImg);
        if (logoImgStr.isEmpty()) return ApiResponse.withError(ErrorCode.ERROR_S3_UPDATE_OBJECT);
        client.setLogoImg(logoImgStr);

        Client updatedClient = clientRepository.save(client);
        return ApiResponse.ok("클라이언트 로고 이미지를 성공적으로 수정했습니다.", updatedClient);
    }

    private String getImgUrl(MultipartFile file) {
        ApiResponse<String> updateFileResponse = s3Adapter.uploadImage(file);

        if(updateFileResponse.getStatus().is5xxServerError()){

            return "";
        }
        String imageUrl = updateFileResponse.getData();
        return imageUrl;
    }

    public ApiResponse deleteClient(Long clientId) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if(optionalClient.isEmpty()){
            return ApiResponse.withError(ErrorCode.INVALID_CLIENT_ID);
        }

        Client client = optionalClient.get();
        clientRepository.delete(client);

        return ApiResponse.ok("클라이언트를 성공적으로 삭제했습니다.");
    }

    public ApiResponse retrieveAllClient() {
        List<Client> clientList = clientRepository.findAll();
        if (clientList.isEmpty()){
            return ApiResponse.ok("클라이언트가 존재하지 않습니다.");
        }

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Client client : clientList) {
            Map<String, Object> responseBody = getResponseBody(client);
            responseList.add(responseBody);
        }

        return ApiResponse.ok("클라이언트 목록을 성공적으로 조회했습니다.", responseList);
    }

    public ApiResponse retrieveClient(Long clientId) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if(optionalClient.isEmpty()){
            return ApiResponse.withError(ErrorCode.INVALID_CLIENT_ID);
        }

        Client client = optionalClient.get();
        Map<String, Object> responseBody = getResponseBody(client);

        return ApiResponse.ok("클라이언트를 성공적으로 조회했습니다.", responseBody);
    }

    public ApiResponse retrieveAllClientLogoImgList() {
        List<Client> clientList = clientRepository.findAll();
        if (clientList.isEmpty()){
            return ApiResponse.ok("클라이언트가 존재하지 않습니다.");
        }

        List<String> logoImgList = new ArrayList<>();
        for (Client client : clientList) {
            logoImgList.add(client.getLogoImg());
        }

        return ApiResponse.ok("클라이언트 로고 이미지 리스트를 성공적으로 조회했습니다.", logoImgList);
    }

    private static Map<String, Object> getResponseBody(Client client) {
        // post와 get의 구조 통일
        LinkedHashMap<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("clientInfo", Map.of(
                "id", client.getId(),
                "name", client.getName()
        ));
        responseBody.put("logoImg", client.getLogoImg());
        return responseBody;
    }
}
