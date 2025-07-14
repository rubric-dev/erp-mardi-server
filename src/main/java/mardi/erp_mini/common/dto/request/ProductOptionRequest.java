package mardi.erp_mini.common.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class ProductOptionRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MoqSearchParam{
        @NotEmpty
        private String brandLineCode;
        private List<String> productCodes;
        private List<String> seasonCodes;
        private List<String> itemCodes;
        private List<String> graphicCodes;
        private String statusCode;
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
        private List<String> seasonCodes;
        private List<String> itemCodes;
        private List<String> graphicCodes;
        private String statusCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LeadTimeUpdate {
        private Long leadTimeId;
        private int leadTime;
    }
}
