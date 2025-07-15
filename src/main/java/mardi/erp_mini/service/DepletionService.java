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
  public List<DepletionResponse.ListRes> getDepletionLevels(Long scenarioId, Long categoryId) {
    return depletionDslRepository.getDepeletionLevels(scenarioId, categoryId);
  }

  @Transactional
  public void updateDepletionLevelParams(Long scenarioId, Long categoryId, DepletionRequest.UpdateParam request) {
    ScenarioItem scenarioItem = depletionDslRepository.getScenarioItem(scenarioId, categoryId, request.getDepletionLevelId());
    scenarioItem.updateParams(request.getGreaterThan(), request.getLesserThan());
    }
}
