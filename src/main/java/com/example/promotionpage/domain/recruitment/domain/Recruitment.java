package com.example.promotionpage.domain.recruitment.domain;

import com.example.promotionpage.domain.recruitment.dto.request.UpdateRecruitmentServiceRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date startDate;

    @NotNull
    private Date deadline;

    @NotNull
    private Boolean status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String qualifications;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String preferential;

    @NotNull
    private String link;

    @Builder
    public Recruitment(String title, Date startDate, Date deadline, String qualifications, String preferential, String link, Date createdAt, Boolean status) {
        this.title = title;
        this.startDate = startDate;
        this.deadline = deadline;
        this.qualifications = qualifications;
        this.preferential = preferential;
        this.link = link;
        this.createdAt = createdAt;
        this.status = status;
    }

    public void update(UpdateRecruitmentServiceRequestDto dto, Boolean status) {
        this.title = dto.title();
        this.startDate = dto.startDate();;
        this.deadline = dto.deadline();;
        this.qualifications = dto.qualifications();
        this.preferential = dto.preferential();
        this.link = dto.link();
        this.status = status;
    }
}
