package mardi.erp_mini.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import mardi.erp_mini.core.response.ReorderResponse.UpdatedBy;
import org.springframework.data.annotation.CreatedBy;

public class ProductResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private Long id;
        private String imageUrl;
        private String name;
        private InfoSeasonDetail season;
        private InfoItemDetail item;
        private String colorCode;
        private String updatedBy;
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

    public static class UpdatedBy{
        Long id;
        String name;
        String imageUrl;
    }

    public static class CreatedBy{
        Long id;
        String name;
        String imageUrl;
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
        CreatedBy createdBy;
        LocalDateTime createdAt;
        UpdatedBy updatedBy;
        LocalDateTime updatedAt;
    }
}
