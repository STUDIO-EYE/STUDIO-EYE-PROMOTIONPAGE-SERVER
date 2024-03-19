package com.example.promotionpage.domain.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.promotionpage.domain.request.domain.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
