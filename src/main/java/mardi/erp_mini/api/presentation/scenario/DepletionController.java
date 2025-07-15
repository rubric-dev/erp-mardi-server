package mardi.erp_mini.api.presentation.scenario;


import java.util.List;
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

  @GetMapping("/{scenarioId}/{categoryId}")
  public CommonResponse<List<ListRes>> getDepletionLevels(@PathVariable Long scenarioId, @PathVariable Long categoryId){
    return new CommonResponse<>(depletionService.getDepletionLevels(scenarioId, categoryId));
  }

  @PutMapping("/{scenarioId}/{categoryId}")
  public CommonResponse updateDepletionParams(@PathVariable Long scenarioId, @PathVariable Long categoryId, @RequestBody DepletionRequest.UpdateParam request){
    depletionService.updateDepletionLevelParams(scenarioId, categoryId, request);
    return CommonResponse.ok();
  }
}
