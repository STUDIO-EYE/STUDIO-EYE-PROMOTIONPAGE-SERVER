package com.example.promotionpage.domain.faq.dto.request;

public record UpdateFaqServiceRequestDto(
        Long id,
        String question,
        String answer,
        Boolean visibility
) {

}
