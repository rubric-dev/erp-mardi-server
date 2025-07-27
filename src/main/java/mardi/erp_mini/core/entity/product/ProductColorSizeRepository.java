package mardi.erp_mini.core.entity.product;

import jakarta.annotation.Nonnull;
import java.util.Optional;
import mardi.erp_mini.common.dto.response.ErrorCode;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductColorSizeRepository extends JpaRepository<ProductColorSize, Long> {
    Optional<ProductColorSize> findByFullProductCode(String fullProductCode);

    @Nonnull
    default ProductColorSize findOneByCode(@Nonnull String code) {
        return this.findByFullProductCode(code).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PRODUCT.getMsg()));
    }
}
