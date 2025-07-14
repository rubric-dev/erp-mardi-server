package mardi.erp_mini.core.entity.product;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionMoqRepository extends JpaRepository<ProductionMoq, Long> {
    @Nonnull
    default ProductionMoq findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("moq not found. id : " + id));
    }

}
