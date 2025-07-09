package mardi.erp_mini.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.core.entity.brand.Brand;
import mardi.erp_mini.core.entity.info.InfoItem;
import mardi.erp_mini.core.entity.info.InfoSeason;
import mardi.erp_mini.core.entity.product.Graphic;

public class ProductResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private Long id;
        private String name;
        private Brand brand;
        private InfoSeason season;
        private InfoItem info;
        private Graphic graphic;
    }
}
