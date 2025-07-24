package mardi.erp_mini.core.entity.product;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.BaseEntity;
import mardi.erp_mini.core.entity.brand.BrandLine;
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

    @Comment("브랜드 라인")
    @JoinColumn(name = "brand_line_cd", referencedColumnName = "code")
    @ManyToOne(fetch = FetchType.LAZY)
    private BrandLine brandLine;

    private String code;

    private String name;

    @ColumnDefault("0")
    private int seq;

    @ColumnDefault("false")
    private boolean isDeleted;

    private LocalDateTime deletedAt;

    public void delete(){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public static Graphic of(String brandLineCode, String code, String name, int seq) {
        return new Graphic(null, BrandLine.builder().code(brandLineCode).build(), code, name, seq, false, null);
    }
}
