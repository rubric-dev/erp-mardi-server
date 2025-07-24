package mardi.erp_mini.core.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Entity
public class ProductColorGraphic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "prod_cd")
    private String productCode;

    @Column(nullable = false, name = "color_cd")
    private String colorCode;

    @Column(nullable = false, name = "graphic_cd")
    private String graphicCode;

    public static ProductColorGraphic of(String productCode, String colorCode, String graphicCode) {
        return new ProductColorGraphic(null, productCode, colorCode, graphicCode);
    }
}
