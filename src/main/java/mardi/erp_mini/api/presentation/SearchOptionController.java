package mardi.erp_mini.api.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.entity.product.SeasonCode;
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
    public CommonResponse<List<SearchOptionResponse.Code>> getBrands(){
        return new CommonResponse<>(searchOptionService.getBrandLines());
    }

    @Operation(summary = "카테고리(item) 목록 조회")
    @GetMapping("/item")
    public CommonResponse<List<SearchOptionResponse.Code>> getInfoItems(){
        return new CommonResponse<>(searchOptionService.getInfoItems());
    }

    @Operation(
            summary = "해당 브랜드의 그래픽 목록 조회",
            description = "조회 가능한 brandLineCode = MFK, MKK"
    )
    @GetMapping("/graphic")
    public CommonResponse<List<SearchOptionResponse.Code>> getGraphics(@RequestParam String brandLineCode){
        return new CommonResponse<>(searchOptionService.getGraphics(brandLineCode));
    }

    @Operation(summary = "유통채널 조회")
    @GetMapping("/distChannel")
    public CommonResponse<List<SearchOptionResponse.Code>> getDistChannels(){
        return new CommonResponse<>(searchOptionService.getDistChannels());
    }

    @Operation(
            summary = "브랜드의 물류 센터 목록 조회",
            description = "조회 가능한 brandLineCode = MFK, MKK"
    )
    @GetMapping("/warehouses")
    public CommonResponse<List<SearchOptionResponse.Id>> getWareHouses(@RequestParam String brandLineCode){
        return new CommonResponse<>(searchOptionService.getWareHouses(brandLineCode));
    }

    @Operation(summary = "소진율 단계 목록 조회")
    @GetMapping("/depletion")
    public CommonResponse<List<SearchOptionResponse.Id>> getDepletionLevels(){
        return new CommonResponse<>(searchOptionService.getDepletionLevels());
    }

    @Operation(summary = "연도 조회")
    @GetMapping("/year")
    public CommonResponse<List<SearchOptionResponse.Code>> getYears(){
        return new CommonResponse<>(searchOptionService.getYears());
    }

    @Operation(summary = "계절 조회")
    @GetMapping("/season")
    public CommonResponse<List<SearchOptionResponse.Code>> getSeasons(){
        return new CommonResponse<>(searchOptionService.getSeasons());
    }

    @Operation(summary = "품목 조회")
    @GetMapping("/product")
    public CommonResponse<List<SearchOptionResponse.Code>> getProducts(@RequestParam String brandLineCode){
        return new CommonResponse<>(searchOptionService.getProducts(brandLineCode));
    }
}
