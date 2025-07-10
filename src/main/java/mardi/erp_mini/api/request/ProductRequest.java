package mardi.erp_mini.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ProductRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SearchParam {
        private String productCode;
        private String name;
        private String brandCode;
        private String seasonCode;
        private String itemCode;
        private String graphicCode;
        private String colorCode;
        private String statusCode;
        private int page;
        private int pageSize;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GraphicGroupSearchParam {
        private Long brandCode;
        private String seasonCode;
        private String itemCode;
        private Long graphicCode;
        private String statusCode;
    }
}
