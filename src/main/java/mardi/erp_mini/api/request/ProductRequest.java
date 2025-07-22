package mardi.erp_mini.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mardi.erp_mini.core.entity.product.StatusCode;

@Getter
@NoArgsConstructor
public class ProductRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SearchParam {
        private List<String> productCodes;
        private List<String> productNames;
        private String brandLineCode;
        private String seasonCode;
        private List<String> itemCodes;
        private List<String> graphicCodes;
        private StatusCode statusCode;
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
