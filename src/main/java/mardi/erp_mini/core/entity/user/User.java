package mardi.erp_mini.core.entity.user;

import jakarta.persistence.*;
import lombok.*;
import mardi.erp_mini.core.entity.auth.UserAuth;
import mardi.erp_mini.security.enums.RoleType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserAuth auth;
    private String name;
    private String email;
    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public boolean isSystemAdmin(){
        return auth.getRole() == RoleType.ADMIN;
    }
}
