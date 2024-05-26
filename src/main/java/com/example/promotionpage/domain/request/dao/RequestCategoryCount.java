package com.example.promotionpage.domain.request.dao;


public interface RequestCategoryCount {
    Integer getYear();
    Integer getMonth();
    String getCategory();
    Long getRequestCount();
}