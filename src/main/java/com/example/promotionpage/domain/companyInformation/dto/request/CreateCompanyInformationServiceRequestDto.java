package com.example.promotionpage.domain.companyInformation.dto.request;

import com.example.promotionpage.domain.companyInformation.domain.CompanyInformation;

import java.util.Map;

public record CreateCompanyInformationServiceRequestDto(
        String mainOverview,
        String commitment,
        String address,
        String phone,
        String fax,
        String introduction,
        Map<String, String> detailInformation
) {
    public CompanyInformation toEntity(String logoImageFileName, String logoImageUrl,
                                       String sloganImageFileName, String sloganImageUrl) {
        return CompanyInformation.builder()
                .mainOverview(mainOverview)
                .commitment(commitment)
                .address(address)
                .phone(phone)
                .fax(fax)
                .introduction(introduction)
                .logoImageFileName(logoImageFileName)
                .logoImageUrl(logoImageUrl)
                .sloganImageFileName(sloganImageFileName)
                .sloganImageUrl(sloganImageUrl)
                .detailInformation(detailInformation)
                .build();
    }

    public UpdateAllCompanyInformationServiceRequestDto toUpdateServiceRequest() {
        return new UpdateAllCompanyInformationServiceRequestDto(mainOverview, commitment, address, phone, fax, introduction, detailInformation);
    }
}
