package mardi.erp_mini.core.entity.product;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.core.entity.Scenario;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionLeadTimeRepository extends JpaRepository<ProductionLeadTime, Long> {
    @Nonnull
    default ProductionLeadTime findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("lead time not found. id : " + id));
    }
}
