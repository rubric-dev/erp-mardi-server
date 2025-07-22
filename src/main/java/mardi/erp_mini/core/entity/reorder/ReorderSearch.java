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
                        @ColumnResult(name = "availableOpenQty", type = Integer.class),
                        @ColumnResult(name = "expectedInboundQty", type = Integer.class),
                        @ColumnResult(name = "periodInboundQty", type = Integer.class),
                        @ColumnResult(name = "periodSalesQty", type = Integer.class),
                        @ColumnResult(name = "monthlyAvgSalesQty", type = Integer.class),
                        @ColumnResult(name = "accExpectedOutboundQty", type = Integer.class),
                        @ColumnResult(name = "availableEndQty", type = Integer.class),
                        @ColumnResult(name = "salesQty", type = Integer.class),
                        @ColumnResult(name = "depletionRate", type = Integer.class),
                        @ColumnResult(name = "sellableDays", type = Integer.class),
                        @ColumnResult(name = "sellableQty", type = Integer.class)
                }
        )
)
@NamedNativeQuery(
        name = "ReorderSearch.nativeQuery",
        resultSetMapping = "reorderSearchMapping",
        query = "select \n" +
                "d.product_color_size_id as \"productColorSizeId\"\n" +
                ",f.firstQty as availableOpenQty -- 기초재고  : 기간 첫날 입고예정\n" +
                ",e.expectedInboundQty as expectedInboundQty --입고예정 : 최신 입고 예\n" +
                ",sum(d.inbound_qty )as periodInboundQty -- 기간입고\n" +
                ",coalesce(sa.sales,0) as periodSalesQty --기간판매\n" +
                ",coalesce(m.average_sale,0) as monthlyAvgSalesQty --월평균판매 \n" +
                ",e.accExpectedOutboundQty as accExpectedOutboundQty --누적 미출고\n" +
                ",f.firstQty + sum(d.inbound_qty)+ e.expectedInboundQty - coalesce(sa.sales,0) as availableEndQty --가용기말재고 날 기초재고 - 기간 판매 + 기간 입고 + 입고 예정 재고)\n" +
                ",coalesce((select sum(sales_qty) from daily_sales dsa where dsa.prod_cd = d.prod_cd),0) as salesQty -- 판매량\n" +
                ",coalesce(sa.sales,0)/ sum(d.inbound_qty) * 100 as depletionRate --소진율 = 누적 판매수량/ 누적 입고수량\n" +
                ",e.todayQty/coalesce(sa.sales_avg,1) as sellableDays -- 판매가능일수\n" +
                ",e.todayQty as sellableQty --판매가능수량\n" +
                "from daily_stock d\n" +
                "left join(\n" +
                "select product_color_size_id, graphic_cd, sum(sales_qty) as sales , avg(sales_qty) as sales_avg\n" +
                "from daily_sales\n" +
                "where \n" +
                "\"date\" between :from and :to\n" +
                "and dist_channel = :distributionChannel\n" +
                "group by product_color_size_id, graphic_cd\n" +
                ") sa\n" +
                " on sa.product_color_size_id = d.product_color_size_id\n" +
                "LEFT JOIN (\n" +
                "    SELECT product_color_size_id, graphic_cd, sum(expected_inbound_qty) as expectedInboundQty, sum(stock_qty) as todayQty, sum(expected_outbound_qty) as accExpectedOutboundQty\n" +
                "    FROM daily_stock \n" +
                "    WHERE \"date\" = (select max(\"date\") from daily_stock d )\n" +
                "    group by product_color_size_id, graphic_cd\n" +
                ") e ON d.product_color_size_id = e.product_color_size_id\n" +
                "and d.graphic_cd  = e.graphic_cd\n" +
                "LEFT JOIN (\n" +
                "    SELECT product_color_size_id, graphic_cd, sum(stock_qty) as firstQty\n" +
                "    FROM daily_stock \n" +
                "    WHERE \"date\" = :from\n" +
                "    group by product_color_size_id, graphic_cd\n" +
                ") f ON d.product_color_size_id = f.product_color_size_id\n" +
                "and d.graphic_cd = f.graphic_cd\n" +
                "left join monthly_stats m\n" +
                "on m.product_color_size_id  = d.product_color_size_id\n" +
                "and m.graphic_cd  = d.graphic_cd\n" +
                "and m.\"month\" = 6\n" +
                "where d.product_color_size_id  in (:products)\n" +
                "and d.date between :from and :to\n" +
                "and d.warehouse_id = :warehouseId \n" +
                "and d.graphic_cd in :graphicCodes\n" +
                "group by d.prod_cd, d.graphic_cd, d.product_color_size_id, f.firstQty, e.expectedinboundqty, e.accexpectedoutboundqty, e.todayqty, sa.sales, sa.sales_avg, m.average_sale\n" +
                ";"
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
    private int monthlyAvgSalesQty;
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
