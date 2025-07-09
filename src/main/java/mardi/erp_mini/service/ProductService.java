package mardi.erp_mini.service;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ProductRequest;
import mardi.erp_mini.core.entity.product.Product;
import mardi.erp_mini.core.entity.product.ProductCustomRepository;
import mardi.erp_mini.core.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductCustomRepository productCustomRepository;

    public PagedModel<ProductResponse.Detail> getProductList(ProductRequest.SearchParam searchParam){
        List<Product> products = productCustomRepository.search(
            searchParam.getProductCode(),
            searchParam.getName(),
            searchParam.getBrandId(),
            searchParam.getSeasonCode(),
            searchParam.getItemCode(),
            searchParam.getGraphicId(),
            searchParam.getStatusCode(),
            searchParam.getPage(),
            searchParam.getPageSize()
        );

        List<ProductResponse.Detail> details = products.stream()
                .map(product -> ProductResponse.Detail.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .brand(product.getBrand())
                        .season(product.getInfoSeason())
                        .info(product.getInfoItem())
                        .graphic(product.getGraphic())
                        .build())
                .toList();

        Page<ProductResponse.Detail> page = new PageImpl<>(details,
            PageRequest.of(
                searchParam.getPage(),
                searchParam.getPageSize()
            ),
            details.size()
        );

        return new PagedModel<>(page);
    }
}
