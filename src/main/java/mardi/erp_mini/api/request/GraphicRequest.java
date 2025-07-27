package mardi.erp_mini.api.request;

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
    private String colorCode;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class SearchParam {
    private String brandLineCode;
    private Integer year;
    private SeasonCode seasonCode;
    private List<String> itemCodes;
    private List<String> productCodes;
    private List<String> productNames;
  }
}
