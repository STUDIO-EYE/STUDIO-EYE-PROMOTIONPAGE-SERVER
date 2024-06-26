package com.example.promotionpage.domain.user.controller;

import com.example.promotionpage.global.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.url}", configuration = FeignConfig.class)
public interface UserFeignClient {
    @GetMapping("/users-id/all")
    List<Long> getAllApprovedUserIds();
}
