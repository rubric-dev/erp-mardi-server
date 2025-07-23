package mardi.erp_mini.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mardi.erp_mini.core.entity.DistributionChannel;
import mardi.erp_mini.core.entity.product.SeasonCode;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReorderRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Create {
        @Schema(description = "상품(SCS단위) id", example = "1")
        private Long productColorSizeId;
        @Schema(description = "요청 수량")
        private int quantity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SearchParam {

        @Schema(description = "브랜드라인 코드", example = "MFK")
        @NotEmpty
        private String brandLineCode;
        @Schema(description = "연도", example = "2024")
        private int year;
        @Schema(description = "시즌 코드", allowableValues = {"1", "2", "3", "4"}, example = "2")
        private SeasonCode seasonCode;
        @Schema(description = "아이템(카테고리) 코드", example = "SS")
        private List<String> itemCodes;
        @Schema(description = "그래픽 코드", example = "FLOWER")
        private List<String> graphicCodes;
        @Schema(description = "상품 코드", example = "MFK42JSS008")
        private List<String> productCodes;
        @Schema(description = "유통 채널", example = "DIRECT")
        private DistributionChannel distChannel;
        @Schema(description = "판매 기간")
        private DateContainer searchDate;
        @Schema(description = "물류 센터 ID", example = "1")
        private Long wareHouseId;
        @Schema(description = "소진율 단계 코드")
        private String depeletionLevel;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DateContainer{
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "시작 날짜", example = "2025-06-01")
        LocalDate from;
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "끝 날짜", example = "2025-06-30")
        LocalDate to;
    }
}
