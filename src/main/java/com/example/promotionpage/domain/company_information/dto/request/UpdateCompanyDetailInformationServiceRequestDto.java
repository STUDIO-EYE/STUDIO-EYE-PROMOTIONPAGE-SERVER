package com.example.promotionpage.domain.company_information.dto.request;

import java.util.Map;

public record UpdateCompanyDetailInformationServiceRequestDto(
        Map<String, String> detailInformation
) {
}
