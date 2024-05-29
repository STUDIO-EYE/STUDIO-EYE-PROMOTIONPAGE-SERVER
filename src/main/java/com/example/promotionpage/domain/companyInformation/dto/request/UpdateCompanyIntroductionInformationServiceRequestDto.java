package com.example.promotionpage.domain.companyInformation.dto.request;

public record UpdateCompanyIntroductionInformationServiceRequestDto(
        String mainOverview, String commitment, String introduction
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
