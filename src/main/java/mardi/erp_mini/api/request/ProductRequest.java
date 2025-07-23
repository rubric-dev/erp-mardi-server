package mardi.erp_mini.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mardi.erp_mini.core.entity.product.SeasonCode;

@Getter
@NoArgsConstructor
public class ProductRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SearchParam {
        private List<String> productCodes;
        private List<String> productNames;
        @Schema(description = "브랜드라인 코드", example = "MFK")
        @NotEmpty
        private String brandLineCode;
        @Schema(description = "연도", example = "2024")
        private int year;
        @Schema(description = "시즌 코드",allowableValues = {"SPRING", "SUMMER", "FALL", "WINTER"}, example = "SUMMER")
        private SeasonCode seasonCode;
        @Schema(description = "아이템(카테고리) 코드", example = "SS")
        private List<String> itemCodes;
        @Schema(description = "그래픽 코드", example = "FLOWER")
        private List<String> graphicCodes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GraphicGroupSearchParam {
        private String brandLineCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SteadySeller {
        @NotEmpty
        private String productCode;
        @NotEmpty
        private String colorCode;
        @NotEmpty
        @JsonProperty("isSteadySeller")
        private boolean isSteadySeller;
    }
}
