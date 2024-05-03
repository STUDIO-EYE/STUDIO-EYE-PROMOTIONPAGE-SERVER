package com.example.promotionpage.domain.companyInformation.dto.request;

import com.example.promotionpage.domain.companyInformation.domain.CompanyInformation;

import java.util.Map;

public record UpdateCompanyBasicInformationServiceRequestDto(
        String address,
        String phone,
        String fax
) {
//    public CompanyInformation toEntity(String logoImageUrl, String sloganImageUrl) {
//        return CompanyInformation.builder()
//                .address(address)
//                .phone(phone)
//                .fax(fax)
//                .logoImageUrl(logoImageUrl)
//                .sloganImageUrl(sloganImageUrl)
//                .build();
//    }
}
