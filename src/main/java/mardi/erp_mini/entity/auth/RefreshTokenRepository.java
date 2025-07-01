package mardi.erp_mini.entity.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByValue(String tokenValue);

    RefreshToken findByValue(String tokenValue);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO refresh_token (user_id, value, email, created_at) " +
            "VALUES (:userId, :tokenValue, :email, now())\n" +
            "ON CONFLICT (user_id)\n" +
            "DO UPDATE SET value = EXCLUDED.value, created_at = now();")
    void upsertValue(Long userId, String tokenValue, String email);
}
