package com.example.promotionpage.domain.menu.application;

import com.example.promotionpage.domain.menu.dao.MenuRepository;
import com.example.promotionpage.domain.menu.domain.Menu;
import com.example.promotionpage.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    public ApiResponse<List<Menu>> retrieveAllMenu() {
        List<Menu> menuList = menuRepository.findAll();
        if(menuList.isEmpty()) {
            return ApiResponse.ok("메뉴가 존재하지 않습니다.");
        }
        return ApiResponse.ok("메뉴 목록을 성공적으로 조회했습니다.", menuList);
    }

    public ApiResponse<List<String>> retrieveMenu() {
        List<String> menuTitleList = menuRepository.findTitleByVisibilityTrue();
        if(menuTitleList.isEmpty()) {
            return ApiResponse.ok("공개된 메뉴가 존재하지 않습니다.");
        }
        return ApiResponse.ok("공개된 메뉴 목록을 성공적으로 조회했습니다.", menuTitleList);
    }
}
