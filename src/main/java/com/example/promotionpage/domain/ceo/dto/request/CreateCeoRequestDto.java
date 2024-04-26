package com.example.promotionpage.domain.ceo.dto.request;

public record CreateCeoRequestDto(
    String name,
    String introduction
) {
    public CreateCeoServiceRequestDto toServiceRequest() {
        return new CreateCeoServiceRequestDto(name, introduction);
    }
}
