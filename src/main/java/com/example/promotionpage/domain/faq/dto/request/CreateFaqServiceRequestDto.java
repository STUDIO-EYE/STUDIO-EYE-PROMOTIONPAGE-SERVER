package com.example.promotionpage.domain.faq.dto.request;

import com.example.promotionpage.domain.faq.domain.Faq;
import com.example.promotionpage.domain.request.domain.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateFaqServiceRequestDto (
    String title,
    String content
) {
    public CreateFaqServiceRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public Faq toEntity() {
        return Faq.builder()
                .title(title)
                .content(content)
                .build();
    }
}
