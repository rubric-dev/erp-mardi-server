package mardi.erp_mini.core.entity.product;

import java.util.List;
import mardi.erp_mini.core.response.ProductResponse.GraphicListRes;

public interface ProductCustomRepository {
    List<ProductColor> search(String productCode, String name, String brandLineCode, String seasonCode, String itemCode, String graphicCode, String statusCode, int page, int pageSize);

    List<GraphicListRes> searchGraphicGroup(String brandLineCode, String seasonCode, List<String> itemCodes, List<String> productCodes, List<String> productNames, String statusCode);
}
