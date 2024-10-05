package com.example.promotionpage.domain.request.dao;


public interface RequestStateCount {
    Integer getYear();
    Integer getMonth();
    String getState();
    Long getRequestCount();
}