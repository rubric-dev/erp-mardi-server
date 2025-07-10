package mardi.erp_mini.core.entity.product;

import jakarta.persistence.*;

@Entity
public class ProductColorGraphic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "product_cd")
    private String productCode;

    @Column(nullable = false, name = "color_cd")
    private String colorCode;

    @Column(nullable = false, name = "graphic_cd")
    private String graphicCode;
}
