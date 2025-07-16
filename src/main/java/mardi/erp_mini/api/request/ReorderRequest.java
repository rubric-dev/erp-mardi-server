package mardi.erp_mini.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mardi.erp_mini.core.entity.product.StatusCode;

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
//        @NotEmpty
        @Schema(description = "시즌 코드", example = "2420")
        private String seasonCode;
        @Schema(description = "아이템(카테고리) 코드", example = "SS")
        private List<String> itemCodes;
        @Schema(description = "그래픽 코드", example = "FLOWER")
        private List<String> graphicCodes;
        @Schema(description = "상품 코드", example = "MFK42JSS008")
        private List<String> productCodes;
        @Schema(description = "유통 채널", example = "DIRECT")
        private String distChannel;
        @Schema(description = "제품 상태 ", example = "CURRENT" )
        private StatusCode statusCode;
        @Schema(description = "판매 기간")
        private DateContainer searchDate;
        @Schema(description = "물류 센터 ID", example = "1")
        private Long wareHouseId;
        @Schema(description = "소진율 단계 코드")
        private String depeletionLevel;
    }

    public static class DateContainer{
        @Schema(description = "시작 날짜", example = "2023-01-01")
        LocalDate from;
        @Schema(description = "끝 날짜", example = "2023-01-01")
        LocalDate to;
    }
}
