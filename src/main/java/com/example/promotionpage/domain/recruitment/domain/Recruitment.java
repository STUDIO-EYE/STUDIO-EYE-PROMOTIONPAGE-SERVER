package com.example.promotionpage.domain.recruitment.domain;

import com.example.promotionpage.domain.recruitment.dto.request.UpdateRecruitmentServiceRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String period;

    @NotNull
    private Boolean status;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String qualifications;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String preferential;

    @Builder
    public Recruitment(String title, String period, String qualifications, String preferential) {
        this.title = title;
        this.period = period;
        this.qualifications = qualifications;
        this.preferential = preferential;
        this.status = true;
    }

    public void update(UpdateRecruitmentServiceRequestDto dto) {
        this.title = dto.title();
        this.period = dto.period();
        this.qualifications = dto.qualifications();
        this.preferential = dto.preferential();
    }
}
