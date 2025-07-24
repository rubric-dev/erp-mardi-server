package mardi.erp_mini.api.presentation.product;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ProductRequest;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.ProductResponse;
import mardi.erp_mini.service.ProductService;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 관리 - 스테디셀러 목록 조회")
    @PostMapping
    public CommonResponse<List<ProductResponse.Detail>> searchProduct(@RequestBody ProductRequest.SearchParam searchParam){
        return new CommonResponse<>(productService.getProductList(searchParam));
    }

    @Operation(summary = "상품 관리 - 스테디셀러 설정/해제")
    @PatchMapping
    public CommonResponse checkSteadySeller(@RequestBody ProductRequest.SteadySeller request){
        productService.setSteadySeller(request);
        return CommonResponse.ok();
    }

}