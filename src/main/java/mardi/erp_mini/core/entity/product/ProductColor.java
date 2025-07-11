package mardi.erp_mini.core.entity.product;

import jakarta.persistence.*;
import lombok.*;
import mardi.erp_mini.common.BaseEntity;
import mardi.erp_mini.core.entity.brand.BrandLine;
import mardi.erp_mini.core.entity.info.InfoItem;
import mardi.erp_mini.core.entity.info.InfoSeason;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductColor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //todo wms상 성별 / 대표코드 명 추가
    @Comment("상품명")
    private String name;
    private String imageUrl;
    private String barcode;

    @Comment("브랜드 라인")
    @JoinColumn(name = "brandLine_cd", referencedColumnName = "code")
    @ManyToOne(fetch = FetchType.LAZY)
    private BrandLine brandLine;

    @Comment("시즌 코드")
    @JoinColumn(name = "season_cd", referencedColumnName = "code")
    @ManyToOne(fetch = FetchType.LAZY)
    private InfoSeason infoSeason;

    @Comment("아이템 코드")
    @JoinColumn(name = "item_cd", referencedColumnName = "code")
    @ManyToOne(fetch = FetchType.LAZY)
    private InfoItem infoItem;

    @Comment("상품 코드")
    @Column(name = "prod_cd")
    private String productCode;

    @Comment("색상 코드")
    @Column(name = "color_cd")
    private String colorCode;

    @Comment("상태 코드")
    private String statusCode;

    @Comment("스테디 셀러 여부")
    @Column(name = "is_steady_seller")
    @ColumnDefault(value = "false")
    private boolean isSteadySeller;
}
