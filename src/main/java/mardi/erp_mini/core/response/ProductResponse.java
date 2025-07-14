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
        Long id;
        String name;
        String code;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoItemDetail{
        Long id;
        String name;
        String code;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GraphicDetail{
        Long id;
        String code;
        String name;
    }

    public static class GraphicListRes {
        int seq;
        GraphicDetail graphic;
        int noOfStyles;
        UserByResponse createdBy;
        LocalDateTime createdAt;
        UserByResponse updatedBy;
        LocalDateTime updatedAt;
    }
}
