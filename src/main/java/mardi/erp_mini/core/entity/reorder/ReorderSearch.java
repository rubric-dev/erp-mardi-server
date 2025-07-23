package mardi.erp_mini.core.entity.reorder;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@SqlResultSetMapping(
        name = "reorderSearchMapping",
        classes = @ConstructorResult(
                targetClass = ReorderSearch.class,
                columns = {
                        @ColumnResult(name = "productColorSizeId", type = Long.class),
                        @ColumnResult(name = "graphicCode", type = String.class),
                        @ColumnResult(name = "availableOpenQty", type = Integer.class),
                        @ColumnResult(name = "expectedInboundQty", type = Integer.class),
                        @ColumnResult(name = "periodInboundQty", type = Integer.class),
                        @ColumnResult(name = "periodSalesQty", type = Integer.class),
                        @ColumnResult(name = "accExpectedOutboundQty", type = Integer.class),
                        @ColumnResult(name = "availableEndQty", type = Integer.class),
                        @ColumnResult(name = "salesQty", type = Integer.class),
                        @ColumnResult(name = "depletionRate", type = Integer.class),
                        @ColumnResult(name = "sellableDays", type = Integer.class),
                        @ColumnResult(name = "sellableQty", type = Integer.class)
                }
        )
)
//TODO: 소진율 단계,리오더 여부 및 마지막 리오더 날짜 추가
@NamedNativeQuery(
        name = "ReorderSearch.nativeQuery",
        resultSetMapping = "reorderSearchMapping",
        query = """
select
    d.product_color_size_id as "productColorSizeId"
    ,f.firstQty as availableOpenQty -- 기초재고  : 기간 첫날 입고예정
    ,e.expectedInboundQty as expectedInboundQty --입고예정 : 최신 입고 예
    ,sum(d.inbound_qty )as periodInboundQty -- 기간입고
    ,coalesce(sa.sales,0) as periodSalesQty --기간판매
    ,sa.salesAvg as dailyAvgSalesQty --일평균판매량
    ,e.accExpectedOutboundQty as accExpectedOutboundQty --누적 미출고
    ,f.firstQty + sum(d.inbound_qty)+ e.expectedInboundQty - coalesce(sa.sales,0) as availableEndQty --가용기말재고 날 기초재고 - 기간 판매 + 기간 입고 + 입고 예정 재고)
    ,coalesce((select sum(sales_qty) from daily_sales dsa where dsa.prod_cd = d.prod_cd and dsa.color_cd = d.color_cd and dsa.graphic_cd = d.graphic_cd),0) as salesQty -- 판매량
    ,coalesce(sa.sales,0)/ sum(d.inbound_qty) * 100 as depletionRate --소진율 = 누적 판매수량/ 누적 입고수량
    ,e.todayQty/coalesce(sa.salesAvg,1) as sellableDays -- 판매가능일수
    ,e.todayQty as sellableQty --판매가능수량
    ,d.graphic_cd as graphicCode
from daily_stock d
    left join(
        select product_color_size_id, graphic_cd, sum(sales_qty) as sales , avg(sales_qty) as salesAvg
        from daily_sales
        where "date" between :from and :to
            and (:distributionChannel is null or dist_channel = :distributionChannel)
        group by product_color_size_id, graphic_cd
    ) sa on sa.product_color_size_id = d.product_color_size_id
    LEFT JOIN (
        SELECT product_color_size_id, graphic_cd, sum(expected_inbound_qty) as expectedInboundQty, sum(stock_qty) as todayQty, sum(expected_outbound_qty) as accExpectedOutboundQty
        FROM daily_stock
        WHERE "date" = (select max("date") from daily_stock d )
        group by product_color_size_id, graphic_cd
    ) e ON d.product_color_size_id = e.product_color_size_id
    and d.graphic_cd  = e.graphic_cd
    LEFT JOIN (
        SELECT product_color_size_id, graphic_cd, sum(stock_qty) as firstQty
        FROM daily_stock
        WHERE "date" = :from
        group by product_color_size_id, graphic_cd
    ) f ON d.product_color_size_id = f.product_color_size_id
    and d.graphic_cd = f.graphic_cd
where (:products is null or d.product_color_size_id  in (:products))
    and d.date between :from and :to
    and (:warehouseId is null or d.warehouse_id = :warehouseId)
    and (:graphicCodes is null or d.graphic_cd in :graphicCodes)
group by d.prod_cd, d.graphic_cd, d.color_cd, d.product_color_size_id, f.firstQty, e.expectedInboundQty, e.accExpectedOutboundQty, e.todayQty, sa.sales, sa.salesAvg
;
"""
)
public class ReorderSearch {
    @Id
    private Long id;
    private Long productColorSizeId;
    private String graphicCode;
    private int availableOpenQty;
    private int expectedInboundQty;
    private int periodInboundQty;
    private int periodSalesQty;
    private int dailyAvgSalesQty;
    private int accExpectedOutboundQty;
    private int availableEndQty;
    private int salesQty;
    private int depletionRate;
//    private String depletionLevel;
    private int sellableDays;
    private int sellableQty;
    private Long reorderById;
    private String reorderByName;
    private String reorderByImageUrl;
    private LocalDateTime reorderAt;
}
