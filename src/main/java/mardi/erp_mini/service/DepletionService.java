package mardi.erp_mini.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.DepletionRequest;
import mardi.erp_mini.core.entity.option.*;
import mardi.erp_mini.core.response.DepletionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DepletionService {
  private final DepletionDslRepository depletionDslRepository;

  @Transactional(readOnly = true)
  public List<DepletionResponse.ListRes> getDepletionLevels(Long scenarioId, String itemCode) {
    return depletionDslRepository.getDepeletionLevels(scenarioId, itemCode);
  }

  @Transactional
  public void updateDepletionLevelParams(Long scenarioId, String itemCode, DepletionRequest.UpdateParam request) {
    ScenarioItem scenarioItem = depletionDslRepository.getScenarioItem(scenarioId, itemCode, request.getDepletionLevelId());
    scenarioItem.updateParams(request.getGreaterThan(), request.getLesserThan());
    }
}
