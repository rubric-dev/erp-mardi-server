package mardi.erp_mini.core.entity.product;

import java.util.List;
import mardi.erp_mini.core.response.ProductResponse;

public interface ProductCustomRepository {
    List<ProductResponse.Detail> search(List<String> productCodes, List<String> productNames, String brandLineCode, int year, SeasonCode seasonCode, List<String> itemCodes, List<String> graphicCodes);

    List<ProductResponse.GraphicGroupListRes> searchGraphicGroup(String brandLineCode);
}
