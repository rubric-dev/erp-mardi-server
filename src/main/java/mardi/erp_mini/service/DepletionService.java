package mardi.erp_mini.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.option.DepletionDslRepository;
import mardi.erp_mini.core.entity.option.DepletionRepository;
import mardi.erp_mini.core.entity.option.ScenarioItemRepository;
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
}
