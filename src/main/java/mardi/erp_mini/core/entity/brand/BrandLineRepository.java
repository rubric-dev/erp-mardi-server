package mardi.erp_mini.core.entity.brand;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandLineRepository extends JpaRepository<BrandLine, Long> {
    @Nonnull
    default BrandLine findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("brandLine not found. id : " + id));
    }

    List<BrandLine> findAllByCodeIn(List<String> codes);
}