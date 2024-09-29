package com.example.promotionpage.domain.menu.api;

import com.example.promotionpage.domain.menu.application.MenuService;
import com.example.promotionpage.domain.menu.domain.Menu;
import com.example.promotionpage.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "메뉴 관리 API", description = "메뉴 수정 / 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @Operation(summary = "PA용 메뉴 전체 조회 API")
    @GetMapping("/menu/all")
    public ApiResponse<List<Menu>> retrieveAllMenu(){
        return menuService.retrieveAllMenu();
    }

    @Operation(summary = "PP용 메뉴 제목 목록 조회 API")
    @GetMapping("/menu")
    public ApiResponse<List<String>> retrieveMenu() {
        return menuService.retrieveMenu();
    }
}
