package mardi.erp_mini.core.entity.product;

import java.util.List;

public interface ProductCustomRepository {
    List<ProductColor> search(String productCode, String name, String brandLineCode, String seasonCode, String itemCode, String graphicCode, String statusCode, int page, int pageSize);
}
