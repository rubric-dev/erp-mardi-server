package mardi.erp_mini.core.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.response.ProductResponse.InfoDetail;

public class GraphicResponse {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ListRes {
    private int seq;
    private InfoDetail graphic;
    private Long noOfStyles;
    private UserByResponse createdBy;
    private LocalDateTime createdAt;
    private UserByResponse updatedBy;
    private LocalDateTime updatedAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ProductDetail {
    private Long id;
    private String imageUrl;
    private String productName;
    private String productCode;
    private int year;
    private String season;
    private boolean isSteadySealer;
    private InfoDetail item;
    private UserByResponse updatedBy;
    private LocalDateTime updatedAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class InfoDetail{
    private Long id;
    private String name;
    private String code;
  }
}
