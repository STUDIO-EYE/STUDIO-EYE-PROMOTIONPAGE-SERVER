package com.example.promotionpage.domain.recruitment.dao;

import com.example.promotionpage.domain.recruitment.domain.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    @Query("SELECT r.id AS id, r.title AS title, r.status AS status FROM Recruitment r")
    Page<RecruitmentTitle> findAllRecruitments(Pageable pageable);

    Optional<Recruitment> findTopByOrderByCreatedAtDesc();

    @Query("SELECT r FROM Recruitment r WHERE r.status = true")
    List<Recruitment> findByStatusTrue();
}
