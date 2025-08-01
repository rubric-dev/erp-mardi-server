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

  @Operation(summary = "상품 그룹 관리 - 그래픽(리스트) 조회", description = "해당 브랜드라인의 그래픽들 목록 조회")
  @GetMapping
  public CommonResponse<List<GraphicResponse.ListRes>> searchGraphic(@RequestParam String brandLineCode){
    return new CommonResponse<>(graphicService.getGraphicGroupList(brandLineCode));
  }

  @Operation(summary = "그래픽 생성하기", description = "새로운 그래픽 생성")
  @PostMapping
  public CommonResponse createGraphic(@RequestBody GraphicRequest.Create request){
    graphicService.createGraphic(request);
    return CommonResponse.ok();
  }

  //TODO: 그래픽 삭제 시 해당 그래픽에 연결된 상품들 처리 방법 체크 후 개발
  @Operation(summary = "그래픽 삭제", description = "그래픽 그룹 삭제. 현재 그래픽 삭제 처리 후 해당 그래픽 사용하는 상품에 대한 처리 미적용")
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

  @Operation(summary = "스타일에 그래픽 등록", description = "스타일 목록 모달 창에서 스타일 단위 상품을 선택하여 해당 상품의 그래픽 등록")
  @PostMapping("/{graphicCode}")
  public CommonResponse createGraphicProduct(@PathVariable String graphicCode, @RequestBody List<GraphicRequest.Product> request){
    graphicService.createGraphicProduct(graphicCode, request);
    return CommonResponse.ok();
  }
}
