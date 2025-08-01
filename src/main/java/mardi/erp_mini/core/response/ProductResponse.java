package mardi.erp_mini.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.entity.product.SeasonCode;

import java.time.LocalDateTime;

public class ProductResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductDetail {
        private Long id;
        private String imageUrl;
        private String productName;
        private String productCode;
        private int year;
        private String season;
        private boolean isSteadySealer;
        private InfoDetail graphic;
        private InfoDetail item;
        private UserByResponse updatedBy;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail{
        private Long id;
        private String imageUrl;
        private String productName;
        private String productCode;
        private int year;
        private String season;
        private InfoDetail item;
        private InfoDetail graphic;
        private UserByResponse updatedBy;
        private LocalDateTime updatedAt;
        private InfoDetail color;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoDetail{
        private Long id;
        private String name;
        private String code;
    }

}
