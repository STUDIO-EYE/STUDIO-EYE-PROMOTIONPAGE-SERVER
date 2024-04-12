package com.example.promotionpage.domain.faq.dao;

import com.example.promotionpage.domain.faq.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    @Query("SELECT f.id AS id, f.question AS question FROM Faq f")
    List<FaqQuestions> findAllQuestions();
}
