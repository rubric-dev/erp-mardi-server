package mardi.erp_mini.core.entity.product;

import java.util.List;

public interface ProductCustomRepository {
    List<Product> search(String productCode, String name, Long brandId, String seasonCode, String itemCode, Long graphicId, String statusCode, int page, int pageSize);
}
