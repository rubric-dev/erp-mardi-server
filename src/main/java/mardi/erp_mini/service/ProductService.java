package mardi.erp_mini.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ProductRequest;
import mardi.erp_mini.api.request.ProductRequest.SteadySeller;
import mardi.erp_mini.core.entity.product.ProductColorRepository;
import mardi.erp_mini.core.entity.product.ProductCustomRepository;
import mardi.erp_mini.core.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductCustomRepository productCustomRepository;
    private final ProductColorRepository productColorRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse.Detail> getProductList(ProductRequest.SearchParam searchParam){
        //TODO: 시즌이 없는 경우 가장 최근 시즌

        List<ProductResponse.Detail> products = productCustomRepository.search(
            searchParam.getProductCodes(),
            searchParam.getProductNames(),
            searchParam.getBrandLineCode(),
            searchParam.getYear(),
            searchParam.getSeasonCode(),
            searchParam.getItemCodes(),
            searchParam.getGraphicCodes()
        );

        return products;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse.GraphicGroupListRes> getGraphicGroupList(ProductRequest.GraphicGroupSearchParam searchParam) {
        List<ProductResponse.GraphicGroupListRes> graphics = productCustomRepository.searchGraphicGroup(searchParam.getBrandLineCode());
        return graphics;
    }

    @Transactional
    public void setSteadySeller(SteadySeller request) {
        productColorRepository.findProductColorByCode(request.getProductCode(), request.getColorCode())
            .updateSteadySeller(request.isSteadySeller());
    }
}
