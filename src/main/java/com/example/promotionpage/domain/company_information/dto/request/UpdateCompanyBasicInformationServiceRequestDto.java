package com.example.promotionpage.domain.company_information.dto.request;

public record UpdateCompanyBasicInformationServiceRequestDto(
        String address,
        String addressEnglish,
        String phone,
        String fax
) {
}
