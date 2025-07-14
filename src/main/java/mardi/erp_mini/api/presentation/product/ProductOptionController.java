package mardi.erp_mini.api.presentation.product;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.request.ProductOptionRequest;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.ProductOptionResponse;
import mardi.erp_mini.service.ProductOptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@RestController
public class ProductOptionController {
    
    private final ProductOptionService productOptionService;

    @PostMapping("/moq")
    public CommonResponse<List<ProductOptionResponse.MoqList>> searchMoqList(@RequestBody ProductOptionRequest.MoqSearchParam searchParam) {
        List<ProductOptionResponse.MoqList> results = productOptionService.getMoqList(searchParam);
        return new CommonResponse<>(results);
    }

    @PutMapping("/moq")
    public CommonResponse updateMoq(@RequestBody ProductOptionRequest.MoqUpdate request) {
        productOptionService.updateMoq(request);
        return CommonResponse.ok();
    }

    @PostMapping("/leadtime")
    public CommonResponse<List<ProductOptionResponse.LeadTimeList>> searchLeadTimeList(@RequestBody ProductOptionRequest.LeadTimeSearchParam searchParam){
        List<ProductOptionResponse.LeadTimeList> results = productOptionService.getLeadTimeList(searchParam);
        return new CommonResponse<>(results);
    }

    @PutMapping("/leadtime")
    public CommonResponse updateLeadTime(@RequestBody ProductOptionRequest.LeadTimeUpdate request){
        productOptionService.updateLeadTime(request);
        return CommonResponse.ok();
    }

}
