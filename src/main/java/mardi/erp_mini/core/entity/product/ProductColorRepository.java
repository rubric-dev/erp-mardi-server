package mardi.erp_mini.core.entity.product;

import java.util.Optional;
import lombok.NonNull;
import mardi.erp_mini.common.dto.response.ErrorCode;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductColorRepository extends JpaRepository<ProductColor, Long> {

  Optional<ProductColor> findProductColorByProductCodeAndInfoColorCode(String productCode, String colorCode);

  @NonNull
  default ProductColor findProductColorByCode(String productCode, String colorCode){
    return this.findProductColorByProductCodeAndInfoColorCode(productCode, colorCode)
        .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PRODUCT.getMsg()));
  }
}
