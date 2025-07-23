package mardi.erp_mini.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mardi.erp_mini.core.entity.product.SeasonCode;

import java.util.List;

public class ProductOptionRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MoqSearchParam{
        @NotEmpty
        private String brandLineCode;
        private List<String> productCodes;
        @Schema(description = "연도", example = "2024")
        private int year;
        @Schema(description = "시즌 코드",allowableValues = {"SPRING", "SUMMER", "FALL", "WINTER"}, example = "SUMMER")
        private List<SeasonCode> seasonCodes;
        @Schema(description = "아이템(카테고리) 코드", example = "SS")
        private List<String> itemCodes;
        @Schema(description = "그래픽 코드", example = "FLOWER")
        private List<String> graphicCodes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MoqUpdate {
        private Long moqId;
        private int qty;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LeadTimeSearchParam {
        @NotEmpty
        private String brandLineCode;
        private List<String> productCodes;
        @Schema(description = "연도", example = "2024")
        private int year;
        @Schema(description = "시즌 코드",allowableValues = {"SPRING", "SUMMER", "FALL", "WINTER"}, example = "SUMMER")
        private List<SeasonCode> seasonCodes;
        @Schema(description = "아이템(카테고리) 코드", example = "SS")
        private List<String> itemCodes;
        @Schema(description = "그래픽 코드", example = "FLOWER")
        private List<String> graphicCodes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LeadTimeUpdate {
        private Long leadTimeId;
        private int leadTime;
    }
}
