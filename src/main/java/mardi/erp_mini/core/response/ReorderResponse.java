package mardi.erp_mini.core.response;

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
        private String productCode;
        private String productName;
        private String productImageUrl;
        private String colorCode;
        private String sizeName;
        private int availableOpenQty;
        private int periodInboundQty;
        private int expectedInboundQty;
        private int periodSalesQty;
        private int monthlyAvgSalesQty;
        private int accExpectedOutboundQty;
        private int availableEndQty;
        private int depletionRate;
        private String depletionLevel;
        private int salesQty;
        private int sellableDays;
        private int sellableQty;
        private int moqQty;
        private int leadTime;
        private String reorderRecommendLevel;
        private Boolean isActiveReorder;
        private UserByResponse reorderBy;
        private LocalDateTime reorderAt;
    }
}