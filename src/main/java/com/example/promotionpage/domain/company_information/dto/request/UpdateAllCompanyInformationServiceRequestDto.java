package com.example.promotionpage.domain.company_information.dto.request;

import java.util.Map;

public record UpdateAllCompanyInformationServiceRequestDto(
        String mainOverview,
        String commitment,
        String address,
        String addressEnglish,
        String phone,
        String fax,
        String introduction,
        Map<String, String> detailInformation
) {
}
