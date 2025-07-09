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
        private Long brandId;
        private String seasonCode;
        private String itemCode;
        private Long graphicId;
        private String colorCode;
        private String statusCode;
        private int page;
        private int pageSize;
    }
}
