package com.example.promotionpage.domain.request.dao;

import java.util.Map;

public interface RequestCategoryCount {
    Integer getYear();
    Integer getMonth();
    String getCategory();
    Long getRequestCount();
}