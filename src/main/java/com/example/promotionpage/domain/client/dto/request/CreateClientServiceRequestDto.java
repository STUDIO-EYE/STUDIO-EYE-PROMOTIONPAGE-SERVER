package com.example.promotionpage.domain.client.dto.request;


import com.example.promotionpage.domain.client.domain.Client;

public record CreateClientServiceRequestDto(
    String name
) {
        public Client toEntity(String logoImg) {
            return Client.builder()
                    .name(name)
                    .logoImg(logoImg)
                    .build();
        }
    }
