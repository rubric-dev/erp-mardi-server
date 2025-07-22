package mardi.erp_mini.core.entity.auth;

import jakarta.annotation.Nonnull;
import java.util.Optional;
import mardi.erp_mini.common.dto.response.ErrorCode;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    boolean existsByUsername(String username);

    Optional<UserAuth> findByUsername(String email);
    @Nonnull
    default UserAuth findOneByUsername(@Nonnull String username) {
        return this.findByUsername(username).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_AUTH.getMsg()));
    }

    @Nonnull
    default UserAuth findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_AUTH.getMsg()));
    }
}
