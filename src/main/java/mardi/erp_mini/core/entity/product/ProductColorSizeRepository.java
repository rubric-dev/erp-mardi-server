package mardi.erp_mini.core.entity.product;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.core.entity.reorder.Reorder;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductColorSizeRepository extends JpaRepository<ProductColorSize, Long> {
    @Nonnull
    default ProductColorSize findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("ProductColorSize not found. id : " + id));
    }
}
