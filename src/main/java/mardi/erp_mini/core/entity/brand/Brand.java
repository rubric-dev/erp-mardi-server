package mardi.erp_mini.core.entity.brand;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    private String name;
    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isDeleted;

    @Builder
    public Brand(Long ownerId, String name, String imageUrl, String code, String industry, String jobType, String employeeScale, String location, Integer foundedYear) {
        this.ownerId = ownerId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void updateOwner(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void update(
            String name,
            String industry,
            String jobType,
            String employeeScale,
            String location,
            Integer foundedYear
    ) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
