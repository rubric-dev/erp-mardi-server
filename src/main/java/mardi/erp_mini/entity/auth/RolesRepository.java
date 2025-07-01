package mardi.erp_mini.entity.auth;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Roles findByName(String name);

    @Nonnull
    default Roles findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("role not found. id : " + id));
    }
}
