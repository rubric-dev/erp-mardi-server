package mardi.erp_mini.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mardi.erp_mini.core.entity.product.Graphic;
import mardi.erp_mini.core.entity.product.SeasonCode;

@Getter
@NoArgsConstructor
public class ProductRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SearchParam {
        @Schema(description = "브랜드라인 코드", example = "MFK")
        private String brandLineCode;
        @Schema(description = "연도", example = "2024")
        private int year;
        @Schema(description = "시즌 코드",allowableValues = {"SPRING", "SUMMER", "FALL", "WINTER"}, example = "SUMMER")
        private SeasonCode seasonCode;
        @Schema(description = "아이템(카테고리) 코드", example = "SS")
        private List<String> itemCodes;
        @Schema(description = "그래픽 코드", example = "FLOWER")
        private List<String> graphicCodes;
        private List<String> productCodes;
        private List<String> productNames;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class ModalSearchParam {
        @Schema(description = "브랜드라인 코드", example = "MFK")
        private String brandLineCode;
        @Schema(description = "연도", example = "2024")
        private Integer year;
        @Schema(description = "시즌 코드",allowableValues = {"SPRING", "SUMMER", "FALL", "WINTER"}, example = "SUMMER")
        private SeasonCode seasonCode;
        @Schema(description = "아이템(카테고리) 코드", example = "SS")
        private List<String> itemCodes;
        @Schema(description = "그래픽 코드", example = "FLOWER")
        private List<String> graphicCodes;
        @Schema(description = "품목 코드(스타일 코드)", example = "MFK42JSS033")
        private List<String> productCodes;
        @Schema(description = "품목 명(스타일 명)", example = "MARDI x NARDIS LOGO T SHIRT BOX FIT")
        private List<String> productNames;
        @Schema(description = "스테디셀러 여부. true 일때 스테디셀러만 검색 false 일때 전부 검색")
        private Boolean isSteadySeller;
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
