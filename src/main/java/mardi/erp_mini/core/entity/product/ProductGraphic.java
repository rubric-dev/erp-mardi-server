package mardi.erp_mini.core.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "product_graphic",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"prod_cd","graphic_cd"}
        )
    }
)
@Entity
public class ProductGraphic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "prod_cd")
    private String productCode;

    @Column(nullable = false, name = "graphic_cd")
    private String graphicCode;

    public static ProductGraphic of(String productCode, String graphicCode) {
        return new ProductGraphic(null, productCode, graphicCode);
    }
}
