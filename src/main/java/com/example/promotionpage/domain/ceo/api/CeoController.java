package com.example.promotionpage.domain.ceo.api;

import com.example.promotionpage.domain.ceo.application.CeoService;
import com.example.promotionpage.global.adapter.S3Adapter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CEO 정보 API", description = "CEO 정보 등록 / 수정 / 삭제 / 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CeoController {
    private final CeoService ceoService;
    private final S3Adapter s3Adapter;
}
