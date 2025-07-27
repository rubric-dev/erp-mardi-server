package mardi.erp_mini.core.entity.reorder;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.BaseEntity;
import mardi.erp_mini.core.entity.brand.BrandLine;
import mardi.erp_mini.core.entity.info.InfoSize;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reorder extends BaseEntity {
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

    @Comment("사이즈 코드")
    @JoinColumn(name = "size_cd", referencedColumnName = "code")
    @ManyToOne(fetch = FetchType.LAZY)
    private InfoSize infoSize;

    @Column(name = "graphic_cd")
    private String graphicCode;

    @Column(name = "full_prod_cd")
    private String fullProductCode;

    @Comment("리오더 수량")
    private int quantity;

    @Comment("리오더 식별 코드")
    private String code;

    @Comment("생산 요청 상태")
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime confirmedAt;
    private Long confirmUserId;

    public enum Status {
        PENDING, COMPLETED, CONFIRRMED, REJECTED, CANCELLED
    }

    @Builder
    public Reorder(BrandLine brandLine, String productCode, String colorCode, InfoSize infoSize,
        String graphicCode, String fullProductCode, int quantity, String code) {
        this.brandLine = brandLine;
        this.productCode = productCode;
        this.colorCode = colorCode;
        this.infoSize = infoSize;
        this.graphicCode = graphicCode;
        this.fullProductCode = fullProductCode;
        this.quantity = quantity;
        this.code = code;
    }

    public void confirm(Long userId){
        confirmedAt = LocalDateTime.now();
        confirmUserId = userId;
    }

}
