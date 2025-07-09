package mardi.erp_mini.core.entity.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //todo wms상 성별 / 대표코드 명 추가

    @Comment("상품명")
    private String name;
    private String imageUrl;
    private Long brandId;
    private int year;

    @Comment("시즌 코드")
    @Column(name = "season_cd")
    private String seasonCode;

    @Comment("아이템 코드")
    @Column(name = "item_cd")
    private String itemCode;
    private Long categoryId;
    private String barCode;

    @Comment("상품 코드")
    @Column(name = "prod_cd")
    private String productCode;

    @Comment("색상 코드")
    @Column(name = "color_cd")
    private String colorCode;

    @Comment("사이즈 코드")
    @Column(name = "size_cd")
    private String sizeCode;

    @Comment("스테디 셀러 여부")
    @Column(name = "is_steady_seller")
    private Boolean isSteadySeller;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
