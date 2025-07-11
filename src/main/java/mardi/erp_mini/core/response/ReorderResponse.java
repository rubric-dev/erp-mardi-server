package mardi.erp_mini.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReorderResponse {
    
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListRes {
        private String brandLineName;
        private String seasonName;
        private String itemName;
        private String productCode;
        private String productName;
        private String colorCode;
        private String sizeName;
        private String productBarcode;
        private int availableOpenQty;
        private int periodInboundQty;
        private int expectedInboundQty;
        private int periodSalesQty;
        private int monthlyAvgSalesQty;
        private int accExpectedOutboundQty;
        private int availableEndQty;
        private int depletionRate;
        private int salesQty;
        private int sellableDays;
        private int sellableQty;
        private int moqQty;
        private int leadTime;
        private int reorderRecommendLevel;
        private Boolean isActiveReorder;
        private UpdatedBy updatedBy;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdatedBy {
        private Long id;
        private String name;
    }
}