package mardi.erp_mini.entity.auth;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.security.enums.RoleType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public UserAuth(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserAuth ofUser(String email, String encPassword) {
        UserAuth partner = of(email, encPassword);
        partner.setRole(RoleType.USER);

        return partner;
    }

    private static UserAuth of(String email, String encPassword) {
        return UserAuth.builder()
                .email(email)
                .password(encPassword)
                .build();
    }

    private void setRole(RoleType role) {
        this.role = role;
    }


    public void modifyPassword(String password) {
        if (StringUtils.hasText(password)) this.password = password;
    }
}
