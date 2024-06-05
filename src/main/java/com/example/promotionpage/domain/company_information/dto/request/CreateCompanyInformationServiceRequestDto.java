package com.example.promotionpage.domain.company_information.dto.request;

import com.example.promotionpage.domain.company_information.domain.CompanyInformation;

import java.util.Map;

public record CreateCompanyInformationServiceRequestDto(
        String mainOverview,
        String commitment,
        String address,
        String addressEnglish,
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
                .addressEnglish(addressEnglish)
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
        return new UpdateAllCompanyInformationServiceRequestDto(mainOverview, commitment, address, addressEnglish, phone, fax, introduction, detailInformation);
    }
}
