package com.example.promotionpage.domain.views.dao;

import com.example.promotionpage.domain.views.domain.Views;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ViewsRepository extends JpaRepository<Views, Long> {
    Optional<Views> findByYearAndMonth(Integer year, Integer month);
    List<Views> findByYear(Integer year);
}
