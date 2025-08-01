package mardi.erp_mini.core.entity.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface ProductGraphicRepository extends JpaRepository<ProductGraphic, Long> {
    @Modifying
    @Transactional
    public int deleteProductGraphicByGraphicCode(String graphicCode);
}
