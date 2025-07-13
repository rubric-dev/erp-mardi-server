package mardi.erp_mini.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ProductRequest;
import mardi.erp_mini.core.entity.info.InfoSeasonRepository;
import mardi.erp_mini.core.entity.product.ProductCustomRepository;
import mardi.erp_mini.core.response.ProductResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductCustomRepository productCustomRepository;
    private final InfoSeasonRepository infoSeasonRepository;

    public List<ProductResponse.Detail> getProductList(ProductRequest.SearchParam searchParam){
        if(searchParam.getSeasonCode() == null|| searchParam.getSeasonCode().isBlank()){
            searchParam.setSeasonCode(infoSeasonRepository.findLatestInfoSeason().getCode());
        }

        List<ProductResponse.Detail> products = productCustomRepository.search(
            searchParam.getProductCodes(),
            searchParam.getProductNames(),
            searchParam.getBrandLineCode(),
            searchParam.getSeasonCode(),
            searchParam.getItemCodes(),
            searchParam.getGraphicCodes(),
            searchParam.getStatusCode()
        );

        return products;
    }

    public List<ProductResponse.GraphicListRes> getGraphicGroupList(ProductRequest.GraphicGroupSearchParam searchParam) {
        List<ProductResponse.GraphicListRes> graphics = productCustomRepository.searchGraphicGroup(
            searchParam.getBrandLineCode(),
            searchParam.getSeasonCode(),
            searchParam.getItemCodes(),
            searchParam.getProductCodes(),
            searchParam.getProductNames(),
            searchParam.getStatusCode()
        );
        return graphics;
    }
}
