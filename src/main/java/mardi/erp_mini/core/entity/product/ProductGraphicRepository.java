package mardi.erp_mini.core.entity.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductGraphicRepository extends JpaRepository<ProductGraphic, Long> {
    @Modifying
    @Transactional
    int deleteProductGraphicByGraphicCode(String graphicCode);
    
    List<ProductGraphic> findByGraphicCodeAndProductCodeIn(String graphicCode, List<String> list);
}
