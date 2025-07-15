package mardi.erp_mini.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;

import java.time.LocalDateTime;

public class DepletionResponse {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ListRes {
    private Long id;
    private String name;
    private int greaterThan;
    private int lesserThan;
    UserByResponse updatedBy;
    LocalDateTime updatedAt;
  }
}
