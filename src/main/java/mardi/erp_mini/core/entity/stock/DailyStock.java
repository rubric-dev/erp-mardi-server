package mardi.erp_mini.core.entity.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

//받아온 데이터가 기준정보에 맞지 않아도 오류 없이 받을 수 있도록 물리적 연관관계 없음
@Entity
public class DailyStock {
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
    @Comment("물류창고 id")
    String ware_house_id;
    @Comment("가용 재고량")
    String stockQty;
    @Comment("입고량")
    String inboundQty;
}
