package com.example.promotionpage.domain.companyInformation.dto.request;

public record UpdateCompanyBasicInformationServiceRequestDto(
        String address,
        String addressEnglish,
        String phone,
        String fax
) {
}
