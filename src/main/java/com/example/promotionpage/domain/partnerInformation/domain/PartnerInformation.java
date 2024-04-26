package com.example.promotionpage.domain.partnerInformation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartnerInformation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String logoImageUrl;

	private Boolean is_main;

	private String link;

	@Builder
	public PartnerInformation(String logoImageUrl, Boolean is_main, String link) {
		this.logoImageUrl = logoImageUrl;
		this.is_main = is_main;
		this.link = link;
	}
}
