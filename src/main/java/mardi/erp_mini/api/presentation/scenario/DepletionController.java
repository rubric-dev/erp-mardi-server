package mardi.erp_mini.api.presentation.scenario;


import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.DepletionResponse.ListRes;
import mardi.erp_mini.service.DepletionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/scenario")
public class DepletionController {

  private final DepletionService depletionService;

  @RequestMapping("/{scenarioId}/{categoryId}")
  public CommonResponse<List<ListRes>> getDepletionLevels(@PathVariable Long scenarioId, @PathVariable Long categoryId){
    return new CommonResponse<>(depletionService.getDepletionLevels(scenarioId, categoryId));
  }

}
