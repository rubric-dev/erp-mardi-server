package mardi.erp_mini.service;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ProductRequest;
import mardi.erp_mini.core.entity.product.ProductColor;
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
        List<ProductColor> products = productCustomRepository.search(
            searchParam.getProductCode(),
            searchParam.getName(),
            searchParam.getBrandLineCode(),
            searchParam.getSeasonCode(),
            searchParam.getItemCode(),
            searchParam.getGraphicCode(),
            searchParam.getStatusCode(),
            searchParam.getPage(),
            searchParam.getPageSize()
        );

        List<ProductResponse.Detail> details = products.stream()
                .map(product -> ProductResponse.Detail.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .imageUrl(product.getImageUrl())
                        .season(
                                ProductResponse.InfoSeasonDetail.builder()
                                        .id(product.getInfoSeason().getId())
                                        .name(product.getInfoSeason().getCode())
                                .build()
                        )
                        .item(
                                ProductResponse.InfoItemDetail.builder()
                                        .id(product.getInfoItem().getId())
                                        .name(product.getInfoItem().getName())
                                        .code(product.getInfoItem().getCode())
                                        .build()
                        )
                        .colorCode(product.getColorCode())
//                        .updatedBy(product.getModifiedBy())
                        .updatedAt(product.getUpdatedAt())
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

    public List<ProductResponse.GraphicListRes> getGraphicGroupList(ProductRequest.GraphicGroupSearchParam searchParam) {
        return null;
    }
}
