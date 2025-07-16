package mardi.erp_mini.core.entity.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @Comment("SCS단위 id")
    Long productColorSizeId;
    @Comment("그래픽 id")
    Long graphicId;
    @Comment("브랜드")
    String brandLineCode;
    @Comment("시즌 코드")
    String season_cd;
    @Comment("아이템(카테고리) 코드")
    String item_cd;
    @Comment("품목 코드")
    String prod_cd;
    @Comment("색상 코드")
    String color_cd;
    @Comment("그래픽 코드")
    String graphic_cd;
    @Comment("사이즈 코드")
    String size_cd;
    @Comment("유통 채널")
    String distChannel;
    @Comment("판매량")
    int salesQty;
}
