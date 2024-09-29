package com.example.promotionpage.domain.menu.dao;

import com.example.promotionpage.domain.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m.title AS title FROM Menu m WHERE m.visibility = true")
    List<String> findTitleByVisibilityTrue();
}
