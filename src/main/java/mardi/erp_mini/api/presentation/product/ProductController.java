package mardi.erp_mini.api.presentation.product;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ProductRequest;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.ProductResponse;
import mardi.erp_mini.service.ProductService;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 그룹 관리 - 스테디셀러")
    @PostMapping
    public CommonResponse<PagedModel<ProductResponse.Detail>> searchProduct(@RequestBody ProductRequest.SearchParam searchParam){
        return new CommonResponse<>(productService.getProductList(searchParam));
    }

    @Operation(summary = "상품 그룹 관리 - 그래픽(리스트)")
    @PostMapping("/graphic")
    public CommonResponse<List<ProductResponse.GraphicListRes>> searchGProductForGraphic(@RequestBody ProductRequest.GraphicGroupSearchParam searchParam){
        return new CommonResponse<>(productService.getGraphicGroupList(searchParam));
    }
}




