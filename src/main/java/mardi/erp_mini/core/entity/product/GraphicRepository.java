package mardi.erp_mini.core.entity.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GraphicRepository extends JpaRepository<Graphic, Long> {
    List<Graphic> findAllByBrandLineCodeOrderByCodeAsc(String brandLineCode);
}
