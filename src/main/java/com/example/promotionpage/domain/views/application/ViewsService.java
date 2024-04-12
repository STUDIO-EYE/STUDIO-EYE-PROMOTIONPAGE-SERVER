package com.example.promotionpage.domain.views.application;

import com.example.promotionpage.domain.views.dao.ViewsRepository;
import com.example.promotionpage.domain.views.domain.Views;
import com.example.promotionpage.domain.views.dto.request.CreateViewsServiceDto;
import com.example.promotionpage.global.common.response.ApiResponse;
import com.example.promotionpage.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
@Transactional
@RequiredArgsConstructor
public class ViewsService {

    private final ViewsRepository viewsRepository;
    // for initial views data / for adding views
    private final Long num1 = 1L;

    public ApiResponse createViews(CreateViewsServiceDto dto) {
        if(!checkMonth(dto.month())) return ApiResponse.withError(ErrorCode.INVALID_VIEWS_MONTH);
        Optional<Views> optionalViews = viewsRepository.findByYearAndMonth(dto.year(), dto.month());
        if(optionalViews.isPresent()) {
            return ApiResponse.withError(ErrorCode.ALREADY_EXISTED_DATA);
        }
        return this.justCreateViews(dto);
    }

    private ApiResponse justCreateViews(CreateViewsServiceDto dto) {
        if(!checkMonth(dto.month())) return ApiResponse.withError(ErrorCode.INVALID_VIEWS_MONTH);
        Views views = dto.toEntity();
        Views savedViews = viewsRepository.save(views);
        return ApiResponse.ok("조회 수 등록을 완료했습니다.", savedViews);
    }

    public ApiResponse retrieveAllViews() {
        List<Views> viewsList = viewsRepository.findAll();
        if(viewsList.isEmpty()) {
            return ApiResponse.ok("조회수가 존재하지 않습니다.");
        }
        return ApiResponse.ok("조회수 목록을 성공적으로 조회했습니다.", viewsList);
    }

    public ApiResponse retrieveViewsByPeriod(Integer startYear, Integer startMonth, Integer endYear, Integer endMonth) {
        // 월 형식 검사
        if(!checkMonth(startMonth) || !checkMonth(endMonth)) return ApiResponse.withError(ErrorCode.INVALID_VIEWS_MONTH);
        // 종료점이 시작점보다 앞에 있을 경우 제한 걸기
        if(startYear > endYear || (startYear.equals(endYear) && startMonth>endMonth)) {
            return ApiResponse.withError(ErrorCode.INVALID_PERIOD_FORMAT);
        }
        // 2~12달로 제한 걸기
        Integer months = (endYear - startYear)*12+(endMonth-startMonth);
        if(months < 2 || months > 12) {
            return ApiResponse.withError(ErrorCode.INVALID_VIEWS_PERIOD);
        }
        List<Views> viewsList = viewsRepository.findByYearAndMonthBetween(startYear, startMonth, endYear, endMonth);
        if(viewsList.isEmpty()) {
            return ApiResponse.ok("조회수가 존재하지 않습니다.");
        }
        return ApiResponse.ok("조회수 목록을 성공적으로 조회했습니다.", viewsList);
    }

    public ApiResponse retrieveViewsByYear(Integer year) {
        List<Views> viewsList = viewsRepository.findByYear(year);
        if(viewsList.isEmpty()) {
            return ApiResponse.ok("조회수가 존재하지 않습니다.");
        }
        return ApiResponse.ok("조회수 목록을 성공적으로 조회했습니다.", viewsList);
    }

    public ApiResponse retrieveViewsByYearMonth(Integer year, Integer month) {
        Optional<Views> optionalViews = viewsRepository.findByYearAndMonth(year, month);
        if(optionalViews.isEmpty()){
            return ApiResponse.ok("조회수가 존재하지 않습니다.");
        }
        Views views = optionalViews.get();
        return ApiResponse.ok("조회수를 성공적으로 조회했습니다.", views);
    }

    public ApiResponse retrieveViewsById(Long viewsId) {
        Optional<Views> optionalViews = viewsRepository.findById(viewsId);
        if(optionalViews.isEmpty()){
            return ApiResponse.ok("조회수가 존재하지 않습니다.");
        }
        Views views = optionalViews.get();
        return ApiResponse.ok("조회수를 성공적으로 조회했습니다.", views);
    }

    public ApiResponse updateViewsById(Long viewsId) {
        Optional<Views> optionalViews = viewsRepository.findById(viewsId);
        if(optionalViews.isEmpty()){
            return ApiResponse.withError(ErrorCode.INVALID_VIEWS_ID);
        }
        Views views = optionalViews.get();
        views.updateViews(views.getViews()+num1);
        Views updatedViews = viewsRepository.save(views);
        return ApiResponse.ok("조회수를 성공적으로 수정했습니다.", updatedViews);
    }

    public ApiResponse updateViewsByYearMonth(Integer year, Integer month) {
        Optional<Views> optionalViews = viewsRepository.findByYearAndMonth(year, month);
        if(optionalViews.isEmpty()){
            // 생성 코드 필요?
            return this.justCreateViews(new CreateViewsServiceDto(year, month, num1));
//            return ApiResponse.withError(ErrorCode.INVALID_VIEWS_ID);
        }
        Views views = optionalViews.get();
        views.updateViews(views.getViews()+num1);
        Views updatedViews = viewsRepository.save(views);
        return ApiResponse.ok("조회수를 성공적으로 수정했습니다.", updatedViews);
    }

    public ApiResponse updateThisMonthViews() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        return this.updateViewsByYearMonth(
                Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date().getTime())),
                Integer.parseInt(new SimpleDateFormat("MM").format(new Date().getTime())));
    }

    private boolean checkMonth(int month) {
        // 월 형식 검사
        if(month<1 || month>12) return false;
        return true;
    }

}
