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
public class ProductionMoq extends BaseEntity {
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

    @ColumnDefault(value = "0")
    @Column(name = "moq_qty")
    private int moqQty;;

    public void update(int moqQty) {
        if(moqQty < 0) throw new IllegalArgumentException();
        this.moqQty = moqQty;
    }
}
