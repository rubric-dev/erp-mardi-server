package mardi.erp_mini.core.entity.brand;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import mardi.erp_mini.common.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class BrandUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String brandlineCode;

    private boolean isDeleted;

    private LocalDateTime deletedAt;

    public void delete(){
        this.isDeleted = false;
        this.deletedAt = LocalDateTime.now();
    }
}
