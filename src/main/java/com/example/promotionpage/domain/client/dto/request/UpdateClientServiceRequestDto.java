package com.example.promotionpage.domain.client.dto.request;

public record UpdateClientServiceRequestDto(
    Long clientId,
    String name
) {
}
