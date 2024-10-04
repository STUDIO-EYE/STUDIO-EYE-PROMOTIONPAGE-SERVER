package com.example.promotionpage.domain.menu.dao;

import com.example.promotionpage.domain.menu.domain.Menu;
import com.example.promotionpage.domain.menu.domain.MenuTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m.menuTitle AS menuTitle FROM Menu m WHERE m.visibility = true ORDER BY m.sequence ASC")
    List<MenuTitle> findTitleByVisibilityTrue();
}
