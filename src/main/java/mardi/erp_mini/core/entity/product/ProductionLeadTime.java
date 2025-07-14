package mardi.erp_mini.core.entity.product;

import jakarta.persistence.*;
import lombok.*;
import mardi.erp_mini.common.BaseEntity;
import mardi.erp_mini.core.entity.brand.BrandLine;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductionLeadTime extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("브랜드 라인")
    @JoinColumn(name = "brand_line_cd ", referencedColumnName = "code")
    @ManyToOne(fetch = FetchType.LAZY)
    private BrandLine brandLine;

    @Comment("상품 코드")
    @Column(name = "prod_cd")
    private String productCode;

    @Comment("색상 코드")
    @Column(name = "color_cd")
    private String colorCode;

    @ColumnDefault(value = "0")
    private int leadTime;

    public void updateLeadTime(int leadTime) {
        if(leadTime < 0) throw new IllegalArgumentException();
        this.leadTime = leadTime;
    }
}
