package mardi.erp_mini.core.entity.user;

import jakarta.persistence.*;
import lombok.*;
import mardi.erp_mini.core.entity.auth.UserAuth;
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
    private String name;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserAuth auth;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isDeleted;
    private LocalDateTime deletedAt;

    public void delete(){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
