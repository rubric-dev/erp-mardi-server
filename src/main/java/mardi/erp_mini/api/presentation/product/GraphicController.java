package mardi.erp_mini.api.presentation.product;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.GraphicRequest;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.GraphicResponse;
import mardi.erp_mini.core.response.ProductResponse;
import mardi.erp_mini.service.GraphicService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/graphic")
public class GraphicController {

  private final GraphicService graphicService;

  @Operation(summary = "상품 그룹 관리 - 그래픽(리스트) 조회")
  @GetMapping
  public CommonResponse<List<GraphicResponse.ListRes>> searchGraphic(@RequestParam String brandLineCode){
    return new CommonResponse<>(graphicService.getGraphicGroupList(brandLineCode));
  }

  @Operation(summary = "그래픽 생성하기")
  @PostMapping
  public CommonResponse createGraphic(@RequestBody GraphicRequest.Create request){
    graphicService.createGraphic(request);
    return CommonResponse.ok();
  }

  @Operation(summary = "그래픽 삭제")
  @DeleteMapping
  public CommonResponse deleteGraphic(@RequestParam String graphicCode){
    graphicService.deleteGraphic(graphicCode);
    return CommonResponse.ok();
  }

  @Operation(summary = "상품 그룹 관리 - 그래픽(상세)", description = "선택한 그래픽에 등록 된 상품 리스트 조회")
  @GetMapping("/{graphicCode}")
  public CommonResponse<List<ProductResponse.Detail>> searchGraphicProduct(@PathVariable String graphicCode, @RequestParam String brandLineCode){
    return new CommonResponse<>(graphicService.getGraphicProductList(graphicCode, brandLineCode));
  }

  @Operation(summary = "스타일 선택 모달", description = "선택한 그래픽에 등록 할 새 상품 리스트 조회")
  @PostMapping("/{graphicCode}/product")
  public CommonResponse<List<ProductResponse.Detail>> searchProductForGraphic(@PathVariable String graphicCode, @RequestBody GraphicRequest.SearchParam searchParam){
    return new CommonResponse<>(graphicService.getProductListForGraphic(graphicCode, searchParam));
  }

  @Operation(summary = "그래픽에 상품 등록")
  @PostMapping("/{graphicCode}")
  public CommonResponse createGraphicProduct(@PathVariable String graphicCode, @RequestBody List<GraphicRequest.Product> request){
    graphicService.createGraphicProduct(graphicCode, request);
    return CommonResponse.ok();
  }
}
