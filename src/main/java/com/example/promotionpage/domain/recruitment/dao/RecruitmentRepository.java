package com.example.promotionpage.domain.recruitment.dao;

import com.example.promotionpage.domain.recruitment.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
}
