package mardi.erp_mini.core.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;

import java.time.LocalDateTime;

public class ReorderResponse {
    
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListRes {
        private String brandLineCode;
        @Schema(description = "상품 이미지 url")
        private String productImageUrl;
        @Schema(description = "상품코드", example = "MFK43SPO010")
        private String productCode;
        @Schema(description = "상품명", example = "SWEATSHIRT FLOWERMARDI NEEDLEWORK")
        private String productName;
        @Schema(description = "컬러", example = "BLK")
        private String colorCode;
        @Schema(description = "사이즈", example = "F")
        private String sizeName;
        @Schema(description = "기초재고")
        private int availableOpenQty;
        @Schema(description = "기간입고")
        private int expectedInboundQty;
        @Schema(description = "입고예정")
        private int periodInboundQty;
        @Schema(description = "기간판매")
        private int periodSalesQty;
        @Schema(description = "월평균판매")
        private int monthlyAvgSalesQty;
        @Schema(description = "누적미출고")
        private int accExpectedOutboundQty;
        @Schema(description = "가용기말재고")
        private int availableEndQty;
        @Schema(description = "판매수량")
        private int salesQty;
        @Schema(description = "소진율", example = "80")
        private int depletionRate;
        @Schema(description = "소진율 단계", example = "매우좋음")
        private String depletionLevel;
        @Schema(description = "판매가능일수", example = "10")
        private int sellableDays;
        @Schema(description = "판매가능수량")
        private int sellableQty;
        @Schema(description = "생산MOQ")
        private int moqQty;
        @Schema(description = "생산 리드타임", example = "10")
        private int leadTime;
        @Schema(description = "리오더 여부", example = "true")
        private String reorderRecommendLevel;
        @Schema(description = "리오더 요청 버튼 표출 여부", example = "true")
        private Boolean isActiveReorder;
        @Schema(description = "요청자")
        private UserByResponse reorderBy;
        @Schema(description = "요청일자", example = "2022-01-01T00:00:00")
        private LocalDateTime reorderAt;
    }
}