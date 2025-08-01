package mardi.erp_mini.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ProductRequest;
import mardi.erp_mini.api.request.ProductRequest.SteadySeller;
import mardi.erp_mini.core.entity.product.ProductColorRepository;
import mardi.erp_mini.core.entity.product.ProductDslRepository;
import mardi.erp_mini.core.entity.product.SeasonCode;
import mardi.erp_mini.core.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductDslRepository productDslRepository;
    private final ProductColorRepository productColorRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse.Detail> getProductList(ProductRequest.SearchParam searchParam){
        //TODO: 시즌이 없는 경우 가장 최근 시즌

        List<ProductResponse.Detail> products = productDslRepository.search(
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

    @Transactional
    public void setSteadySeller(SteadySeller request) {
        productColorRepository.findProductColorByCode(request.getProductCode(), request.getColorCode())
            .updateSteadySeller(request.isSteadySeller());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse.ProductDetail> getProducts(ProductRequest.ModalSearchParam searchParam) {

        if (searchParam.getYear() == null || searchParam.getYear() == 0){
            searchParam.setYear(LocalDate.now().getYear());
        }

        if(searchParam.getSeasonCode() == null) {
            searchParam.setSeasonCode(SeasonCode.recentSeasonCode());
        }

        return productDslRepository.getProducts(
                searchParam.getBrandLineCode(),
                searchParam.getYear(),
                searchParam.getSeasonCode(),
                searchParam.getItemCodes(),
                searchParam.getGraphicCodes(),
                searchParam.getProductCodes(),
                searchParam.getProductNames(),
                searchParam.getIsSteadySeller()
        );
    }
    @Transactional(readOnly = true)
    public List<ProductResponse.Detail> getProductColors(ProductRequest.ModalSearchParam searchParam) {

        if (searchParam.getYear() == null || searchParam.getYear() == 0){
            searchParam.setYear(LocalDate.now().getYear());
        }

        if(searchParam.getSeasonCode() == null) {
            searchParam.setSeasonCode(SeasonCode.recentSeasonCode());
        }

        return productDslRepository.getProductColors(
                searchParam.getBrandLineCode(),
                searchParam.getYear(),
                searchParam.getSeasonCode(),
                searchParam.getItemCodes(),
                searchParam.getGraphicCodes(),
                searchParam.getProductCodes(),
                searchParam.getProductNames(),
                searchParam.getIsSteadySeller()
        );
    }
}
