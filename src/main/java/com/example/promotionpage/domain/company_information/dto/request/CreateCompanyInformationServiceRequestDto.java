package com.example.promotionpage.domain.company_information.dto.request;

import com.example.promotionpage.domain.company_information.domain.CompanyInformation;
import com.example.promotionpage.domain.company_information.domain.CompanyInformationDetailInformation;

import java.util.ArrayList;
import java.util.List;

public record CreateCompanyInformationServiceRequestDto(
        String mainOverview,
        String commitment,
        String address,
        String addressEnglish,
        String phone,
        String fax,
        String introduction,
        List<DetailInformationDTO> detailInformation
) {
    public CompanyInformation toEntity(String logoImageFileName, String logoImageUrl,
                                       String sloganImageFileName, String sloganImageUrl) {
        CompanyInformation.CompanyInformationBuilder builder = CompanyInformation.builder()
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
                .sloganImageUrl(sloganImageUrl);

        if (detailInformation != null) {
            List<CompanyInformationDetailInformation> companyInformationDetails = new ArrayList<>();
            for (DetailInformationDTO dto : detailInformation) {
                companyInformationDetails.add(CompanyInformationDetailInformation.builder()
                        .key(dto.getKey())
                        .value(dto.getValue())
                        .build());
            }
            builder.detailInformation(companyInformationDetails);
        }

        return builder.build();
    }

    public UpdateAllCompanyInformationServiceRequestDto toUpdateServiceRequest() {
        return new UpdateAllCompanyInformationServiceRequestDto(mainOverview, commitment, address, addressEnglish, phone, fax, introduction, detailInformation);
    }
}
