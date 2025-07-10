package mardi.erp_mini.core.entity.reorder;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.core.entity.auth.Roles;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReorderRepository extends JpaRepository<Reorder, Long> {
    @Nonnull
    default Reorder findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("reorder not found. id : " + id));
    }
}
