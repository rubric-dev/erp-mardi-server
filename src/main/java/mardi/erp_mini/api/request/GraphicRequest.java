package mardi.erp_mini.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  }

}
