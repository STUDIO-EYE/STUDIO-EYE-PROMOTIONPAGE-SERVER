package com.example.promotionpage.domain.user.service;

import com.example.promotionpage.domain.user.controller.UserFeignClient;
import com.example.promotionpage.domain.user.dto.GetUserResponseDto;
import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl {

    private final UserFeignClient userFeignClient;

    public List<Long> getAllApprovedUserIds() {
        try {
            System.out.println(userFeignClient.getAllApprovedUserIds());
            return userFeignClient.getAllApprovedUserIds();
        }
        catch (Exception e){
            System.out.println("null");
            return null;
        }
    }

}
