package com.example.promotionpage.domain.client.api;

import com.example.promotionpage.domain.client.application.ClientService;
import com.example.promotionpage.domain.client.dto.request.CreateClientRequestDto;
import com.example.promotionpage.domain.client.dto.request.UpdateClientRequestDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "클라이언트 API", description = "클라이언트 등록 / 수정 / 삭제 / 조회")
@RestController
// TODO 추후 /api를 /admin으로 변경해야 한다.
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "클라이언트 등록 API")
    @PostMapping("/client")
    public ApiResponse createClient(@Valid @RequestPart("clientInfo") CreateClientRequestDto dto,
                                    @RequestPart(value = "logoImg", required = false) MultipartFile logoImg){
        return clientService.createClient(dto.toServiceRequest(), logoImg);
    }

    @Operation(summary = "클라이언트 수정 API")
    @PutMapping("/client")
    public ApiResponse updateClient(@Valid @RequestPart("clientInfo") UpdateClientRequestDto dto,
                                    @RequestPart(value = "logoImg", required = false) MultipartFile logoImg){
        return clientService.updateClient(dto.toServiceRequest(), logoImg);
    }

    @Operation(summary = "클라이언트 로고 이미지 수정 API")
    @PutMapping("/client/{clientId}/logoImg")
    public ApiResponse updateClientLogoImg(@PathVariable Long clientId,
                                    @RequestPart(value = "logoImg", required = false) MultipartFile logoImg){
        return clientService.updateClientLogoImg(clientId, logoImg);
    }

    @Operation(summary = "클라이언트 삭제 API")
    @DeleteMapping("/client/{clientId}")
    public ApiResponse deleteClient(@PathVariable Long clientId){
        return clientService.deleteClient(clientId);
    }

    @Operation(summary = "클라이언트 전체 조회 API")
    @GetMapping("/client")
    public ApiResponse retrieveAllClient(){
        return clientService.retrieveAllClient();
    }

    @Operation(summary = "클라이언트 상세 조회 API")
    @GetMapping("/client/{clientId}")
    public ApiResponse retrieveClient(@PathVariable Long clientId){
        return clientService.retrieveClient(clientId);
    }


}
