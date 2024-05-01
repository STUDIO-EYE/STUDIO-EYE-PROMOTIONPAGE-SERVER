package com.example.promotionpage.domain.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.promotionpage.domain.request.domain.Request;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByState(Integer state);
    Long countByState(Integer state);
    // 기간 중 request 수
    @Query("SELECT r.year AS year, r.month AS month, COUNT(r) AS requestCount " +
            "FROM Request r " +
            "WHERE (r.year > :startYear OR (r.year = :startYear AND r.month >= :startMonth)) " +
            "AND (r.year < :endYear OR (r.year = :endYear AND r.month <= :endMonth))" +
            "GROUP BY r.year, r.month")
    List<RequestCount> findByYearAndMonthBetween(@Param("startYear") Integer startYear,
                                                 @Param("startMonth") Integer startMonth,
                                                 @Param("endYear") Integer endYear,
                                                 @Param("endMonth") Integer endMonth);

    // 기간 중 category마다의 request 수
    @Query("SELECT r.year AS year, r.month AS month, r.category AS category, COUNT(r) AS requestCount " +
            "FROM Request r " +
            "WHERE (r.year > :startYear OR (r.year = :startYear AND r.month >= :startMonth)) " +
            "AND (r.year < :endYear OR (r.year = :endYear AND r.month <= :endMonth)) " +
            "GROUP BY r.year, r.month, r.category")
    List<RequestCategoryCount> findCategoryReqNumByYearAndMonthBetween(@Param("startYear") Integer startYear,
                                                                        @Param("startMonth") Integer startMonth,
                                                                        @Param("endYear") Integer endYear,
                                                                        @Param("endMonth") Integer endMonth);
}
