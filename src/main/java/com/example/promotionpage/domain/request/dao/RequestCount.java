package com.example.promotionpage.domain.request.dao;

import com.example.promotionpage.domain.request.domain.State;

public interface RequestCount {
    Integer getYear();
    Integer getMonth();
    Long getRequestCount();
    String getCategory();
    State getState();
}
