package mardi.erp_mini.core.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ReorderResponse {
    
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListRes {
        private Long productColorSizeId;
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
        @Schema(description = "그래픽코드", example = "FLOWER")
        private String graphicName;
        @Schema(description = "생산MOQ")
        private int moqQty;
        @Schema(description = "생산 리드타임", example = "10")
        private int leadTime;
        @Schema(description = "기초재고")
        private int availableOpenQty;
        @Schema(description = "입고예정")
        private int expectedInboundQty;
        @Schema(description = "기간입고")
        private int periodInboundQty;
        @Schema(description = "일평균판매")
        private int dailyAvgSalesQty;
        @Schema(description = "기간판매")
        private int periodSalesQty;
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
        @Schema(description = "리오더 여부", example = "true")
        private String reorderRecommendLevel;
        @Schema(description = "리오더 요청 버튼 표출 여부", example = "true")
        private Boolean isActiveReorder;
        @Schema(description = "요청자")
        private UserByResponse reorderBy;
        @Schema(description = "요청일자", example = "2022-01-01T00:00:00")
        private LocalDateTime reorderAt;

        public void setStats(
                int availableOpenQty,
                int expectedInboundQty,
                int periodInboundQty,
                int dailyAvgSalesQty,
                int periodSalesQty,
                int accExpectedOutboundQty,
                int availableEndQty,
                int salesQty,
                int depletionRate,
                int sellableDays,
                int sellableQty) {
            this.availableOpenQty = availableOpenQty;
            this.expectedInboundQty = expectedInboundQty;
            this.periodInboundQty = periodInboundQty;
            this.dailyAvgSalesQty = dailyAvgSalesQty;
            this.periodSalesQty = periodSalesQty;
            this.accExpectedOutboundQty = accExpectedOutboundQty;
            this.availableEndQty = availableEndQty;
            this.salesQty = salesQty;
            this.depletionRate = depletionRate;
            this.sellableDays = sellableDays;
            this.sellableQty = sellableQty;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product{
        private Long productColorSizeId;
        private String productImageUrl;
        private String productCode;
        private String productName;
        private String colorCode;
//        private String colorName;
        private String sizeCode;
        private String sizeName;
        private String graphicCode;
//        private String graphicName;
        private int moqQty;
        private int leadTime;
    }

    @Getter
    @NoArgsConstructor
    public static class User{
        private Long productColorSizeId;
        private String graphicCode;
        private Long id;
        private String name;
        private String imageUrl;
        private LocalDateTime updatedAt;

        @Builder
        public User(Long productColorSizeId, String graphicCode, Long id, String name, String imageUrl, Timestamp updatedAt) {
            this.productColorSizeId = productColorSizeId;
            this.graphicCode = graphicCode;
            this.id = id;
            this.name = name;
            this.imageUrl = imageUrl;
            this.updatedAt = updatedAt.toLocalDateTime();
        }
    }
}