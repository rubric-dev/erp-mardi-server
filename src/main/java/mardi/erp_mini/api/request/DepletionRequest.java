package mardi.erp_mini.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class DepletionRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Create {
    private Long productItem;
    private Long scenarioId;
    private String depletionLevelName;
    private int greaterThan;
    private int lesserThan;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class UpdateParam {
    private Long depletionLevelId;
    private int greaterThan;
    private int lesserThan;
  }
}
