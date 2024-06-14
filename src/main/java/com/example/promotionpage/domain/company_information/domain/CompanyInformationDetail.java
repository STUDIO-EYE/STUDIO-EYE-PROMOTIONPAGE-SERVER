package com.example.promotionpage.domain.company_information.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyInformationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_information_id")
    @JsonBackReference
    private CompanyInformation companyInformation;

    @Column(name = "detail_information_key")
    private String key;

    @Column(name = "detail_information")
    private String value;


//    public CompanyInformationDetail() {}

    @Builder
    public CompanyInformationDetail(CompanyInformation companyInformation, String key, String value) {
        this.companyInformation = companyInformation;
        this.key = key;
        this.value = value;
    }
}
