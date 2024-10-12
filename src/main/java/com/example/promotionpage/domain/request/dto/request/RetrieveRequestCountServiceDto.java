package com.example.promotionpage.domain.request.dto.request;

public record RetrieveRequestCountServiceDto(
		Integer startYear,
		Integer startMonth,
		Integer endYear,
		Integer endMonth
) {
}
