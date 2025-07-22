package mardi.erp_mini.core.entity.user;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Nonnull
    default User findOneByUsername(@Nonnull String username) {
        return this.findByUsername(username).orElseThrow(() -> new NotFoundException("사용자가 없습니다. username : " + username));
    }

    @Nonnull
    default User findOneById(Long userId) {
        return this.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found. id : " + userId));
    }

    List<User> findAll();

}
