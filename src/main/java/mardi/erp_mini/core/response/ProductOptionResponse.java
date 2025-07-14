package mardi.erp_mini.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ProductOptionResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LeadTimeList {
        private Long id;
        private String imageUrl;
        private String colorCode;
        private String colorName;
        private String productCode;
        private String productName;
        private int leadTime;
        private LocalDateTime updatedAt;
        private ScenarioResponse.UserContainer updateUser;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MoqList {
        private Long id;
        private String imageUrl;
        private String colorCode;
        private String colorName;
        private String productCode;
        private String productName;
        private int qty;
        private LocalDateTime updatedAt;
        private ScenarioResponse.UserContainer updateUser;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserContainer {
        private Long id;
        private String name;
        private String imageUrl;
    }

}
