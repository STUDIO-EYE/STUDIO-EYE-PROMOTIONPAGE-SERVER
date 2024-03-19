package com.example.promotionpage.domain.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.promotionpage.domain.project.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
