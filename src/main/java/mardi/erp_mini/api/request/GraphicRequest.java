package mardi.erp_mini.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mardi.erp_mini.core.entity.product.SeasonCode;

public class GraphicRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Create{
    private String brandLineCode;
    private String name;
    private String code;
    private int seq;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Product{
    private String productCode;
    @Schema(description = "컬러코드. 없는 경우 해당 품목코드(스타일단위)의 모든 색상에 반영")
    private String colorCode;
  }

}
