package mardi.erp_mini.core.entity.brand;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Nonnull
    default Brand findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("brand not found. id : " + id));
    }

    List<Brand> findAllByCodeIn(List<String> codes);
}