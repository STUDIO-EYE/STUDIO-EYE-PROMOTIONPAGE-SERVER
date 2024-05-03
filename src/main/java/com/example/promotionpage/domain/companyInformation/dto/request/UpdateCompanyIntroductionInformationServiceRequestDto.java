package com.example.promotionpage.domain.companyInformation.dto.request;

import com.example.promotionpage.domain.companyInformation.domain.CompanyInformation;

import java.util.Map;

public record UpdateCompanyIntroductionInformationServiceRequestDto(
        String introduction
) {
//    public CompanyInformation toEntity(String logoImageUrl, String sloganImageUrl) {
//        return CompanyInformation.builder()
//                .address(address)
//                .phone(phone)
//                .fax(fax)
//                .introduction(introduction)
//                .logoImageUrl(logoImageUrl)
//                .sloganImageUrl(sloganImageUrl)
//                .detailInformation(detailInformation)
//                .build();
//    }
}
