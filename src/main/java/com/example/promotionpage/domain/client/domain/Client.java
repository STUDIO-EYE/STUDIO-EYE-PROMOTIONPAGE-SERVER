package com.example.promotionpage.domain.client.domain;

import com.example.promotionpage.domain.client.dto.request.UpdateClientServiceRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String logoImg;

    @Builder
    public Client(String name, String logoImg) {
        this.name = name;
        this.logoImg = logoImg;
    }

    public Client update(UpdateClientServiceRequestDto dto) {
        this.name = dto.name();
        return this;
    }
}
