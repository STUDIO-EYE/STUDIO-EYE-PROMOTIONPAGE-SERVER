package com.example.promotionpage.domain.user.service;

import com.example.promotionpage.domain.user.controller.UserFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl {

    private final UserFeignClient userFeignClient;

    public List<Long> getAllApprovedUserIds() {
        try {
            return userFeignClient.getAllApprovedUserIds();
        }
        catch (Exception e){
            return null;
        }
    }

}
