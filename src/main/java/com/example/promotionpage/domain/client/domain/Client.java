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

    private Boolean visibility;

    @Builder
    public Client(String name, String logoImg, Boolean visibility) {
        this.name = name;
        this.logoImg = logoImg;
        this.visibility = visibility;
    }

    public Client update(UpdateClientServiceRequestDto dto) {
        this.name = dto.name();
        this.visibility = dto.visibility();
        return this;
    }
}
