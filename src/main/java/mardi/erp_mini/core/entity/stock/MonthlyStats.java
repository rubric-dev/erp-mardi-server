package mardi.erp_mini.core.entity.stock;

import jakarta.persistence.*;

@Entity
public class MonthlyStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int year;
    int month;
    Long productColorSizeId;
    @Column(name = "graphic_cd")
    String graphicCode;
    @Column(name = "prod_cd")
    String productCode;
    @Column(name = "brand_line_cd")
    String brandLineCode;
    @Column(name = "season_cd")
    String seasonCode;
    @Column(name = "item_cd")
    String itemCode;
    @Column(name = "color_cd")
    String colorCode;
    @Column(name = "size_cd")
    String sizeCode;
    int totalSale;
    int averageSale;
}
