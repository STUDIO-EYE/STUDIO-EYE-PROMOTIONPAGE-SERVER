package com.example.promotionpage.domain.ceo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

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

    @ElementCollection
    private List<String> imageUrlList = new LinkedList<>();

    @Builder
    public Ceo(String name, String introduction, List<String> imageUrlList) {
        this.name = name;
        this.introduction = introduction;
        this.imageUrlList = imageUrlList;
    }
}
