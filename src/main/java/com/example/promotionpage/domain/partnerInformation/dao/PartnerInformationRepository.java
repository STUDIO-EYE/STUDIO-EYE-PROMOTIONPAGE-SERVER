package com.example.promotionpage.domain.partnerInformation.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.promotionpage.domain.partnerInformation.domain.PartnerInformation;

public interface PartnerInformationRepository extends JpaRepository<PartnerInformation, Long> {

}
