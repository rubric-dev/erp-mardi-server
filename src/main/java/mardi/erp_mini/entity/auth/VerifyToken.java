package mardi.erp_mini.entity.auth;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.exception.ExpiredTokenException;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class VerifyToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String email;

    @Embedded
    private Phone phone;

    private String value;

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    @Builder
    public VerifyToken(Type type, String email, String value, LocalDateTime createdAt, LocalDateTime expiredAt, Phone phone) {
        this.type = type;
        this.email = email;
        this.phone = phone;
        this.value = value;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }

    public static VerifyToken of(String email, Type type, LocalDateTime createdAt) {
        return VerifyToken.builder()
                .email(email)
                .type(type)
                .value(UUID.randomUUID().toString())
                .createdAt(createdAt)
                .expiredAt(createdAt.plusDays(7))
                .build();
    }

    public static VerifyToken ofPasswordReset(String email, LocalDateTime createdAt) {
        return VerifyToken.builder()
                .email(email)
                .type(Type.USER_PASSWORD_RESET)
                .value(UUID.randomUUID().toString())
                .createdAt(createdAt)
                .expiredAt(createdAt.plusDays(7))
                .build();
    }

    public static VerifyToken createWithValue(String email, Type type, LocalDateTime createdAt, String value) {
        return VerifyToken.builder()
                .email(email)
                .type(type)
                .value(value)
                .createdAt(createdAt)
                .expiredAt(createdAt.plusDays(7))
                .build();
    }

    public static VerifyToken ofValidPhone(Phone phone, LocalDateTime createdAt) {
        return VerifyToken.builder()
                .phone(phone)
                .type(Type.VERIFY_PHONE)
                .value(generateRandomNumberString())
                .createdAt(createdAt)
                .expiredAt(createdAt.plusDays(7))
                .build();
    }

    public enum Type {
        USER_SIGN_UP, USER_PASSWORD_RESET, CREATOR_PASSWORD_RESET, VERIFY_PHONE
    }

    public void validateExpired() {
        if (isExpired()) throw new ExpiredTokenException();
    }

    private boolean isExpired() {
        return this.expiredAt.isBefore(LocalDateTime.now());
    }

    public void extendExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    private static String generateRandomNumberString() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;
        return String.valueOf(number);
    }
}
