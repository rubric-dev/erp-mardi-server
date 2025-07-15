package mardi.erp_mini.api.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.SearchOptionResponse;
import mardi.erp_mini.service.SearchOptionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/options")
@RestController
public class SearchOptionController {

    private final SearchOptionService searchOptionService;

    @Operation(summary = "사용자가 소속된 브랜드 목록 조회")
    @GetMapping("/brand")
    public CommonResponse<List<SearchOptionResponse>> getBrands(){
        return new CommonResponse<>(searchOptionService.getBrandLines());
    }

    @Operation(summary = "카테고리(item) 목록 조회")
    @GetMapping("/item")
    public CommonResponse<List<SearchOptionResponse>> getInfoItems(){
        return new CommonResponse<>(searchOptionService.getInfoItems());
    }

    @Operation(summary = "그래픽 목록 조회")
    @GetMapping("/graphic")
    public CommonResponse<List<SearchOptionResponse>> getGraphics(@RequestParam String brandLineCode){
        return new CommonResponse<>(searchOptionService.getGraphics(brandLineCode));
    }

    @Operation(summary = "제품 상태 목록 조회")
    @GetMapping("/status")
    public CommonResponse<List<SearchOptionResponse>> getStatus(){
        return new CommonResponse<>(searchOptionService.getStatus());
    }
}
