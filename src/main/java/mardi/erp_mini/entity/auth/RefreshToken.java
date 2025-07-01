package mardi.erp_mini.entity.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String email;
    private String value;
    private LocalDateTime createdAt;

    @Builder
    public RefreshToken(String value, String email, Long userId, LocalDateTime createdAt) {
        this.value = value;
        this.userId = userId;
        this.createdAt = createdAt;
        this.email = email;
    }

    public static RefreshToken of(String value, String email, Long userId) {
        return RefreshToken.builder()
                .value(value)
                .userId(userId)
                .email(email)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static RefreshToken of(String value, Long userId) {
        return RefreshToken.builder()
                .value(value)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
