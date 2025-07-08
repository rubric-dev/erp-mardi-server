package mardi.erp_mini.core.entity.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerifyTokenRepository extends JpaRepository<VerifyToken, Long> {
//    boolean existsByEmailAndRole(String email, RoleType role);
//    Optional<AuthUser> findByEmailAndRole(String email, RoleType role);

    Optional<VerifyToken> findByValue(String value);

    Optional<VerifyToken> findByEmailAndType(String email, VerifyToken.Type type);

}
