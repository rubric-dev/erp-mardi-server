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
    @JoinColumn(name = "brandLine_cd", referencedColumnName = "code")
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

    private int quantity;

    //todo 자체생성 ? 값 받기?
    private String code;
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime confirmedAt;
    private Long confirmUserId;

    public enum Status {
        PENDING, COMPLETED
    }

    @Builder
    public Reorder(BrandLine brandLine, String productCode, String colorCode, InfoSize infoSize, int quantity, String code, Status status, LocalDateTime confirmedAt, Long confirmUserId) {
        this.brandLine = brandLine;
        this.productCode = productCode;
        this.colorCode = colorCode;
        this.infoSize = infoSize;
        this.quantity = quantity;
        this.code = code;
        this.status = Status.PENDING;
        this.confirmedAt = confirmedAt;
        this.confirmUserId = confirmUserId;
    }

    public void confirm(Long userId){
        confirmedAt = LocalDateTime.now();
        confirmUserId = userId;
    }

}
