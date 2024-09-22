package com.example.promotionpage.domain.news.dao;

import com.example.promotionpage.domain.news.domain.NewsFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsFileRepository extends JpaRepository<NewsFile, Long> {

}
