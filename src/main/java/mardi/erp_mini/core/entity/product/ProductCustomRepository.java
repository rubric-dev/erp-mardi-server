package mardi.erp_mini.core.entity.product;

import java.util.List;
import mardi.erp_mini.core.response.ProductResponse;

public interface ProductCustomRepository {

    List<ProductResponse.Detail> search(List<String> productCodes, List<String> productNames, String brandLineCode, String seasonCode, List<String> itemCodes, List<String> graphicCodes, String statusCode);
}
