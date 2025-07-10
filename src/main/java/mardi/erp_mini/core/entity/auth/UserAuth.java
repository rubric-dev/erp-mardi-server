package mardi.erp_mini.core.entity.auth;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.BaseEntity;
import mardi.erp_mini.security.enums.RoleType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserAuth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Builder
    public UserAuth(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static UserAuth ofUser(String username, String email, String encPassword) {
        UserAuth partner = of(username, email, encPassword);
        partner.setRole(RoleType.USER);

        return partner;
    }

    private static UserAuth of(String username, String email, String encPassword) {
        return UserAuth.builder()
                .username(username)
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
