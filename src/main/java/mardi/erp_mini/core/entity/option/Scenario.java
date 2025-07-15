package mardi.erp_mini.core.entity.option;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.BaseEntity;
import mardi.erp_mini.core.entity.brand.BrandLine;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Scenario extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Comment("브랜드")
    @JoinColumn(name = "brand_line_cd", referencedColumnName = "code")
    @ManyToOne(fetch = FetchType.LAZY)
    private BrandLine brandLine;
    private String name;

    @ColumnDefault("false")
    private boolean isActive;

    public void activate(){
        isActive = true;
    }

    public void deactivate() {
        isActive = false;
    }

    private boolean isDeleted;
    private LocalDateTime deletedAt;

    public void delete(){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    @Builder
    public Scenario(BrandLine brandLine, String name) {
        this.brandLine = brandLine;
        this.name = name;
    }
}
