package mardi.erp_mini.core.entity.stock;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailySales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Comment("날짜")
    LocalDate date;
    @Comment("상세 품목 코드")
    @Column(name= "full_prod_cd")
    String fullProductCode;
    @Column(name= "graphic_cd")
    @Comment("그래픽 코드")
    String grapicCode;
    @Column(name= "brand_line_cd")
    @Comment("브랜드")
    String brandLineCode;
    @Column(name= "season_cd")
    @Comment("시즌 코드")
    String seasonCode;
    @Column(name= "item_cd")
    @Comment("아이템(카테고리) 코드")
    String itemCode;
    @Column(name= "prod_cd")
    @Comment("품목 코드")
    String productCode;
    @Column(name= "color_cd")
    @Comment("색상 코드")
    String colorCode;
    @Column(name= "size_cd")
    @Comment("사이즈 코드")
    String sizeCode;
    @Column(name = "dist_channel")
    @Comment("유통 채널")
    String distChannel;
    @Column(name = "sales_qty")
    @Comment("판매량")
    int salesQty;
}
