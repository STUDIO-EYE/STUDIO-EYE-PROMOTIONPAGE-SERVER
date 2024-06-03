package com.example.promotionpage.domain.companyInformation.dto.request;

import java.util.Map;

public record UpdateCompanyDetailInformationServiceRequestDto(
        Map<String, String> detailInformation
) {
}
