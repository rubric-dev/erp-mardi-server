package mardi.erp_mini.api.presentation.scenario;


import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.DepletionRequest;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.DepletionResponse.ListRes;
import mardi.erp_mini.service.DepletionService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/scenario")
public class DepletionController {

  private final DepletionService depletionService;

  @Operation(summary = "소진율 설정 목록 조회")
  @GetMapping("/{scenarioId}/{itemCode}")
  public CommonResponse<List<ListRes>> getDepletionLevels(@PathVariable Long scenarioId, @PathVariable String itemCode){
    return new CommonResponse<>(depletionService.getDepletionLevels(scenarioId, itemCode));
  }

  @Operation(summary = "소진율 구간 범위 수정")
  @PutMapping("/{scenarioId}/{itemCode}")
  public CommonResponse updateDepletionParams(@PathVariable Long scenarioId, @PathVariable String itemCode, @RequestBody DepletionRequest.UpdateParam request){
    depletionService.updateDepletionLevelParams(scenarioId, itemCode, request);
    return CommonResponse.ok();
  }
}
