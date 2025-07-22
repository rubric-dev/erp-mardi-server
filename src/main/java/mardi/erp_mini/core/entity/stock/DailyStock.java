package mardi.erp_mini.core.entity.stock;

import jakarta.persistence.*;
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
    @Column(name= "warehouse_id")
    @Comment("물류창고 id")
    Long warehouseId;
    @Column(name= "stock_qty")
    @Comment("가용 재고량")
    int stockQty;
    @Column(name= "inbound_qty")
    @Comment("입고량")
    int inboundQty;
    @Column(name= "outbound_qty")
    @Comment("출고량")
    int outboundQty;
    @Column(name= "expected_outbound_qty")
    @Comment("미출고")
    int expectedOutboundQty;
    @Column(name= "expected_inbound_qty")
    @Comment("Comment")
    int expectedInboundQty;
}
