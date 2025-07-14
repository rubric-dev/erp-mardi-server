package mardi.erp_mini.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;

import java.time.LocalDateTime;

public class ProductOptionResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LeadTimeList {
        private Long id;
        private String brandLineCode;
        private String imageUrl;
        private String colorCode;
        private String productCode;
        private String productName;
        private int leadTime;
        private UserByResponse updatedBy;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MoqList {
        private Long id;
        private String brandLineCode;
        private String imageUrl;
        private String productCode;
        private String productName;
        private String colorCode;
        private int moqQty;
        private UserByResponse updatedBy;
        private LocalDateTime updatedAt;
    }

}
