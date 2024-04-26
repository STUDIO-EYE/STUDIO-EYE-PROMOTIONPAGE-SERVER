package com.example.promotionpage.domain.ceo.dao;

import com.example.promotionpage.domain.ceo.domain.Ceo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CeoRepository extends JpaRepository<Ceo, Long> {
}
