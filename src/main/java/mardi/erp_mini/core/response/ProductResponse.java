package mardi.erp_mini.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;

import java.time.LocalDateTime;

public class ProductResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private Long id;
        private String imageUrl;
        private String productName;
        private String productCode;
        private String colorCode;
        private InfoSeasonDetail season;
        private InfoItemDetail item;
        private UserByResponse updatedBy;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoSeasonDetail{
        private Long id;
        private String name;
        private String code;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoItemDetail{
        private Long id;
        private String name;
        private String code;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GraphicDetail{
        private Long id;
        private String code;
        private String name;
    }

    public static class GraphicListRes {
        private int seq;
        private GraphicDetail graphic;
        private int noOfStyles;
        private UserByResponse createdBy;
        private LocalDateTime createdAt;
        private UserByResponse updatedBy;
        private LocalDateTime updatedAt;
    }
}
