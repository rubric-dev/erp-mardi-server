package mardi.erp_mini.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        private UpdatedBy updatedBy;
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
    public static class UpdatedBy{
        private Long id;
        private String name;
        private String imageUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GraphicDetail{
        Long id;
        String name;
    }

    public static class GraphicListRes {


    }
}
