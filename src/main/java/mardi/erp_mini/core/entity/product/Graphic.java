package mardi.erp_mini.core.entity.product;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.BaseEntity;
import mardi.erp_mini.core.entity.brand.Brand;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Graphic extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("브랜드")
    @JoinColumn(name = "brand_cd", referencedColumnName = "code")
    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;

    private String name;

    @ColumnDefault("0")
    private int seq;

    @ColumnDefault("false")
    private boolean isActive;
}
