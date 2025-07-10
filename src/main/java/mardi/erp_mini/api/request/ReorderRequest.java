package mardi.erp_mini.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ReorderRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Create {
        private Long productColorSizeId;
        private Long brandId;
        private int quantity;
    }
}
