package com.example.promotionpage.domain.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.promotionpage.domain.request.domain.Request;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
//    @Query("SELECT r.year AS year, r.month AS month, COUNT(r) AS requestCount " +
//            "FROM Request r " +
//            "WHERE (r.year = :startYear AND r.month >= :startMonth) " +
//            "OR (r.year = :endYear AND r.month <= :endMonth) " +
//            "OR (r.year > :startYear AND r.year < :endYear) " +
//            "GROUP BY r.year, r.month")
    @Query("SELECT r.year AS year, r.month AS month, COUNT(r) AS requestCount " +
            "FROM Request r " +
            "WHERE (r.year > :startYear OR (r.year = :startYear AND r.month >= :startMonth)) " +
            "AND (r.year < :endYear OR (r.year = :endYear AND r.month <= :endMonth))" +
            "GROUP BY r.year, r.month")
    List<RequestCount> findByYearAndMonthBetween(@Param("startYear") Integer startYear,
                                                 @Param("startMonth") Integer startMonth,
                                                 @Param("endYear") Integer endYear,
                                                 @Param("endMonth") Integer endMonth);
}
