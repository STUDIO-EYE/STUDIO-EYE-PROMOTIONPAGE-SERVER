package com.example.promotionpage.domain.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.promotionpage.domain.project.domain.Project;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.projectImages")
    List<Project> findAllWithImages();
    List<Project> findByProjectType(String projectType);
}