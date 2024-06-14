package com.example.promotionpage.domain.company_information.dto.request;

import java.util.List;

public record UpdateCompanyDetailInformationServiceRequestDto(
        List<DetailInformationDTO> detailInformation
) {
}
