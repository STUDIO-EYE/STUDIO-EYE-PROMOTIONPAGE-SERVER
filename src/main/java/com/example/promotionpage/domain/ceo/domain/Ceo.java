package com.example.promotionpage.domain.ceo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ceo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String introduction;

    @NotNull
    private String imageUrl;

    @Builder
    public Ceo(String name, String introduction, String imageUrl) {
        this.name = name;
        this.introduction = introduction;
        this.imageUrl = imageUrl;
    }
}
