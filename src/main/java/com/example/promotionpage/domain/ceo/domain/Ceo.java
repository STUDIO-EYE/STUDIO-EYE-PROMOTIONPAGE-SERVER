package com.example.promotionpage.domain.ceo.domain;

import com.example.promotionpage.domain.ceo.dto.request.UpdateCeoServiceRequestDto;
import jakarta.persistence.*;
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
    private String imageFileName;

    @NotNull
    private String imageUrl;

    @Builder
    public Ceo(String name, String introduction, String imageFileName, String imageUrl) {
        this.name = name;
        this.introduction = introduction;
        this.imageFileName = imageFileName;
        this.imageUrl = imageUrl;
    }

    public void updateCeoInformation(UpdateCeoServiceRequestDto dto, String imageFileName, String imageUrl) {
        this.name = dto.name();
        this.introduction = dto.introduction();
        this.imageFileName = imageFileName;
        this.imageUrl = imageUrl;
    }
}
