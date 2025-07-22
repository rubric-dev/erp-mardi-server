package mardi.erp_mini.core.entity.auth;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.exception.NotFoundException;
import mardi.erp_mini.security.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    boolean existsByEmailAndRole(String email, RoleType role);

    Optional<UserAuth> findByEmailAndRole(String email, RoleType role);
    Optional<UserAuth> findByEmail(String email);
    Optional<UserAuth> findByUsername(String email);
    @Nonnull
    default UserAuth findOneByUsername(@Nonnull String username) {
        return this.findByUsername(username).orElseThrow(() -> new NotFoundException("사용자 권한이 없습니다. username : " + username));
    }

    @Nonnull
    default UserAuth findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("사용자 권한이 없습니다. id : " + id));
    }
}
