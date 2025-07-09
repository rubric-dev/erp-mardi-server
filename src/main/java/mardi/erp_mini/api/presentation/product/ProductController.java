package mardi.erp_mini.api.presentation.product;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ProductRequest;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.ProductResponse;
import mardi.erp_mini.service.ProductService;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 목록 조회")
    @GetMapping
    public CommonResponse<PagedModel<ProductResponse.Detail>> searchProduct(@ModelAttribute ProductRequest.SearchParam searchParam){
        return new CommonResponse<>(productService.getProductList(searchParam));
    }
}




