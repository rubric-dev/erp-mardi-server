package mardi.erp_mini.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.DepletionRequest;
import mardi.erp_mini.core.entity.option.*;
import mardi.erp_mini.core.response.DepletionResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DepletionService {
  private final DepletionRepository depletionRepository;
  private final ScenarioItemRepository scenarioItemRepository;
  private final DepletionDslRepository depletionDslRepository;

  public List<DepletionResponse.ListRes> getDepletionLevels(Long scenarioId, Long categoryId) {
    return depletionDslRepository.getDepeletionLevels(scenarioId, categoryId);
  }

  public void updateDepletionLevelParams(Long scenarioId, Long categoryId, DepletionRequest.UpdateParam request) {
    ScenarioItem scenarioItem = depletionDslRepository.getScenarioItem(scenarioId, categoryId, request.getDepletionLevelId());
    scenarioItem.updateParams(request.getGreaterThan(), request.getLesserThan());
    scenarioItemRepository.save(scenarioItem);
  }
}
