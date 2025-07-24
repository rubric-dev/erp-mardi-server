package mardi.erp_mini.core.entity.option;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.core.entity.brand.BrandLine;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    @Nonnull
    default Scenario findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("scenario not found. id : " + id));
    }

    Scenario findByBrandLineCodeAndIsActive(String brandLineCode, boolean isActive);

    List<Scenario> findByBrandLine(BrandLine brandLine);
}
